package com.example.bloodbank.UI.Blood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodbank.Adapter.BloodRequestAdapter
import com.example.bloodbank.databinding.FragmentBloodRequestBinding
import com.example.bloodbank.model.Request
import com.example.bloodbank.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class BloodRequestFragment : Fragment(){

    private lateinit var binding: FragmentBloodRequestBinding
    lateinit var arr: ArrayList<Request>
    lateinit var map: HashMap<Request, String>
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private var logRef = db.collection("Logs")
    lateinit var bloodRealTimeDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        arr = ArrayList()
        map = HashMap()
        binding = FragmentBloodRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bloodRequestList.adapter = BloodRequestAdapter(arr ,map)
        binding.bloodRequestList.layoutManager = LinearLayoutManager(requireContext())
        binding.bloodRequestList.setHasFixedSize(true)
        bloodRealTimeDbRef = FirebaseDatabase.getInstance().getReference("BloodBanks")
        initiateList()
        Log.i("Guitar", "onViewCreated")

    }

    private fun initiateList() {

        val currentUserId = auth.currentUser?.uid
        var userObj = User()
        logRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val temp = document.toObject<User>()!!
                if (temp.user_id == currentUserId) {
                    userObj = temp
                    break
                }
            }
        }

        Log.i("Guitar", "initiateList")

        bloodRealTimeDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arr.clear()
                map.clear()
                for (userSnapshot in snapshot.children) {
                    val requestItem = userSnapshot.getValue(Request::class.java)
                    if (requestItem?.hospitalAddress == userObj.place) {
                        arr.add(requestItem)
                        map.put(requestItem, userSnapshot.key.toString())
                    }
                }
                (binding.bloodRequestList.adapter as BloodRequestAdapter).updateData(arr ,map)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(), "Error while updataing the list", Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

}