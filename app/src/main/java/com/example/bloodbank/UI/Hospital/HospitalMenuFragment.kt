package com.example.bloodbank.UI.Hospital

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodbank.Adapter.HospitalRequestAdapter
import com.example.bloodbank.databinding.FragmentMenuHospitalBinding
import com.example.bloodbank.model.Request
import com.example.bloodbank.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class HospitalMenuFragment : Fragment() {

    lateinit var binding: FragmentMenuHospitalBinding
    lateinit var arr: ArrayList<Request>
    lateinit var hospitalRealTimeDbRef: DatabaseReference
    private var db = FirebaseFirestore.getInstance()
    private var logRef = db.collection("Logs")
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var deleteList: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMenuHospitalBinding.inflate(inflater, container, false)
        arr = ArrayList()
        deleteList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserId = auth.currentUser?.uid
        var userObj = User()
        logRef.whereEqualTo("user_id", currentUserId).get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                userObj = document.toObject(User::class.java)!!
            }
            val currentDB = userObj.name.replace(" ", "").trim()
            val path = "Hospitals/${currentDB}"
            hospitalRealTimeDbRef = FirebaseDatabase.getInstance().getReference(path)
            binding.hospitalRequestList.adapter = HospitalRequestAdapter(arr)
            binding.hospitalRequestList.layoutManager = LinearLayoutManager(requireContext())
            binding.hospitalRequestList.setHasFixedSize(true)

            hospitalRealTimeDbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    deleteList.clear()
                    arr.clear()
                    for (userSnapshot in snapshot.children) {
                        val requestItem = userSnapshot.getValue(Request::class.java)
                        Log.i("Guitar", "Hospital Menu fragment requestItem : $requestItem")
                        arr.add(requestItem!!)
                        if(requestItem.status == "accepted" || requestItem.status == "declined"){
                            deleteList.add(userSnapshot.key.toString())
                        }
                    }
                    (binding.hospitalRequestList.adapter as HospitalRequestAdapter).updateData(arr)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(), "Error while updataing the list", Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        binding.deleteRequest.setOnClickListener{
            deleteRequest()
        }
    }

    private fun deleteRequest(){
        for(item in deleteList){
            hospitalRealTimeDbRef.child(item).removeValue()
        }
    }
}