package com.example.bloodbank.UI.Hospital

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.bloodbank.Adapter.spinnerAdapter
import com.example.bloodbank.databinding.FragmentHosHomeBinding
import com.example.bloodbank.model.Request
import com.example.bloodbank.model.User
import com.example.bloodbank.utils.GenerateID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class HospitalHomeFragment : Fragment() {

    lateinit var bloodAdapter: spinnerAdapter
    lateinit var unitAdapter: spinnerAdapter
    var bloodList: ArrayList<String> = ArrayList()
    var unitList: ArrayList<String> = ArrayList()
    lateinit var binding: FragmentHosHomeBinding
    private var db = FirebaseFirestore.getInstance()
    lateinit var hospitalRealTimeDbRef: DatabaseReference
    private var logRef = db.collection("Logs")
    private lateinit var path: String
    private  var auth: FirebaseAuth=  FirebaseAuth.getInstance()
    lateinit var bloodRealTimeDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bloodList.clear()
        unitList.clear()
        initiateList()
        bloodAdapter = spinnerAdapter(requireContext(), bloodList)
        unitAdapter = spinnerAdapter(requireContext(), unitList)
        binding = FragmentHosHomeBinding.inflate(inflater, container, false)
        binding.bloodSpinner.adapter = bloodAdapter
        binding.unitSpinner.adapter = unitAdapter

        binding.hospitalRequestbtn.setOnClickListener {
            onRequest()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                requireActivity().finish()
                auth.signOut()
                }
            }
            )

        return binding.root
    }

    private fun onRequest() {

        val patientName = binding.patientNameEd.text.toString()
        val address = binding.location.text.toString()
        val hospitalName = binding.hospitalName.text.toString()
        val hyperLink = path
        val bloodType : String = bloodList.get(binding.bloodSpinner.selectedItem.toString().toInt())
        val units : String = unitList.get(binding.unitSpinner.selectedItem.toString().toInt())
        val hashCode = GenerateID.generate()
        val obj = Request(patientName, hospitalName, address, bloodType, units, hyperLink ,"pending" , hashCode ,"","0")

        if (patientName.isBlank() || patientName.isEmpty()) {
            Toast.makeText(requireContext(), "Enter the valid Patient Name", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val ref = hospitalRealTimeDbRef.push()
        ref.setValue(obj)
        val newReferenceId = ref.key
        val updateMap = mapOf("referenceId" to newReferenceId)
        hospitalRealTimeDbRef.child(newReferenceId.toString()).updateChildren(updateMap)
        obj.referenceId = newReferenceId.toString()
        bloodRealTimeDbRef.push().setValue(obj)

    }

    private fun initiateList() {

        bloodList.add("A+")
        bloodList.add("A-")
        bloodList.add("B+")
        bloodList.add("B-")
        bloodList.add("AB+")
        bloodList.add("AB-")
        bloodList.add("RH+")
        bloodList.add("RH-")
        bloodList.add("O+")
        bloodList.add("O-")

        unitList.add("1")
        unitList.add("2")
        unitList.add("3")
        unitList.add("4")
        unitList.add("5")

        logRef = db.collection("Logs")

        val currentUserId = auth.currentUser?.uid
        var userObj = User()
        logRef.whereEqualTo("user_id", currentUserId).get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                userObj = document.toObject(User::class.java)!!
            }
            binding.hospitalName.text = userObj.name
            binding.location.text = userObj.place
            val currentDB = userObj.name.replace(" ", "").trim()
            path = "Hospitals/${currentDB}"

        hospitalRealTimeDbRef = FirebaseDatabase.getInstance ().getReference(path)
        bloodRealTimeDbRef = FirebaseDatabase.getInstance().getReference("BloodBanks")

    }
    }
}