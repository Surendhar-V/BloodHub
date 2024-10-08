package com.example.bloodbank.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodbank.R
import com.example.bloodbank.databinding.BloodRequestItemBinding
import com.example.bloodbank.model.Request
import com.example.bloodbank.utils.BloodRequestInterface
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BloodRequestAdapter(var arr: ArrayList<Request>, var map: HashMap<Request, String>) :
    RecyclerView.Adapter<BloodRequestAdapter.ViewHolder>(), BloodRequestInterface {

    fun updateData(newList: ArrayList<Request>, newMap: HashMap<Request, String>) {
        arr = newList
        map = newMap
        Log.i("Guitar", "updateData map:  $map")
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: BloodRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Request) {
            binding.requestItem = item

            binding.declineBtn.isEnabled = true
            binding.acceptBtn.isEnabled = true


            binding.acceptBtn.setOnClickListener {
                onAcceptPressed(item)
            }
            binding.declineBtn.setOnClickListener {
                binding.declineBtn.isEnabled = false
                onDeclinePressed(item)
            }
            when (item.bloodType) {
                "A+" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.a_pos)
                }

                "B+" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.b_pos)
                }

                "B-" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.b_neg)
                }

                "AB+" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.ab_pos)
                }

                "AB-" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.ab_neg)
                }

                "RH+" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.rh_pos)
                }

                "RH-" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.rh_neg)
                }

                "O+" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.o_pos)
                }

                "O-" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.o_neg)
                }

                "A-" -> {
                    binding.bloodReqImage.setImageResource(R.drawable.a_neg)
                }

                else -> {
                    binding.bloodReqImage.setImageResource(R.drawable.blood_drop)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = BloodRequestItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = arr.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Request = arr[position]
        holder.bind(item)
    }

    override fun onAcceptPressed(request: Request) {
        val bloodRealTimeDbRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("BloodBanks")
        bloodRealTimeDbRef.child(map.get(request)!!).removeValue()
        val statusUpdateMap = mapOf("status" to "accepted")
        var hospitalName = request.hospitalName
        hospitalName = hospitalName.replace(" ", "")
        val hospitalRealTimeDbRef =
            FirebaseDatabase.getInstance().getReference("Hospitals").child(hospitalName)
                .child(request.referenceId)
        hospitalRealTimeDbRef.updateChildren(statusUpdateMap)
    }

    override fun onDeclinePressed(request: Request) {

        CoroutineScope(Dispatchers.Main).launch {
            var hospitalName = request.hospitalName
            hospitalName = hospitalName.replace(" ", "")

            val hospitalRealTimeDbRef =
                FirebaseDatabase.getInstance().getReference("Hospitals").child(hospitalName)
                    .child(request.referenceId)

            var count = "0"

            try {
                val dataSnapshot = hospitalRealTimeDbRef.child("declineCount").get().await()
                if (dataSnapshot.exists()) {
                    count = dataSnapshot.getValue(String::class.java) ?: "0"
                    Log.i("guitar", "count from hospital database (decline count) $count")
                } else {
                    Log.d("Guitar", "Decline count not found in the database.")
                }
            } catch (exception: Exception) {
                Log.e("guitar", "Error retrieving decline count: $exception")
            }

            count = (count.toInt() + 1).toString()

            Log.i("guitar", "onDeclinePressed: $count")
            val bloodRealTimeDbRef = FirebaseDatabase.getInstance().getReference("BloodBanks")

            bloodRealTimeDbRef.child(map.get(request)!!)
                .updateChildren(mapOf("declineCount" to count))

            val countUpdateMap = mapOf("declineCount" to "${count}")
            hospitalRealTimeDbRef.updateChildren(countUpdateMap)

            if (count.toInt() >= 2) {

                val statusUpdateMap = mapOf("status" to "declined")
                hospitalRealTimeDbRef.updateChildren(statusUpdateMap)

                val bloodRealTimeDbRef: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("BloodBanks")
                bloodRealTimeDbRef.child(map.get(request)!!).removeValue()

            }

        }
    }
}
