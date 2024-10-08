package com.example.bloodbank.UI.Login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bloodbank.UI.Hospital.HospitalActivity
import com.example.bloodbank.databinding.FragmentLoginHospitalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HospitalLoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginHospitalBinding
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    private var logRef = db.collection("Logs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginHospitalBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hospitalLogin.setOnClickListener {
            loginUser()
        }

    }

    private fun loginUser() {

        if (binding.emailHosLogin.text.toString().isBlank() || binding.emailHosLogin.text.toString()
                .isEmpty() ||
            binding.passwordHosLogin.text.toString()
                .isEmpty() || binding.passwordHosLogin.text.toString()
                .isBlank()
        ) {
            Toast.makeText(requireContext(), "Enter the valid User Credentials", Toast.LENGTH_SHORT)
                .show()
            return
        }




        auth.signInWithEmailAndPassword(
            binding.emailHosLogin.text.toString().trim(),
            binding.passwordHosLogin.text.toString().trim()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                logRef.whereEqualTo("user_id", auth.uid).get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            if (document.getString("entry_name") == "Hospital") {
                                val intent = Intent(requireContext(), HospitalActivity::class.java)
                                startActivity(intent)
                                return@addOnSuccessListener
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            "Enter the valid user credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Enter the valid User Credentials",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}