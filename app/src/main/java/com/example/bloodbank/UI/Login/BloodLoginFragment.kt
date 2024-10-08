package com.example.bloodbank.UI.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bloodbank.UI.Blood.BloodActivity
import com.example.bloodbank.databinding.FragmentLoginBloodBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BloodLoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBloodBinding
    private lateinit var auth : FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    private var logRef = db.collection("Logs")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBloodBinding.inflate(inflater , container , false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bloodBankLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser(){

        if(binding.emailBb.text.toString().isBlank() || binding.emailBb.text.toString().isEmpty() ||
            binding.passwordBb.text.toString().isEmpty() || binding.passwordBb.text.toString().isBlank()){
            Toast.makeText(requireContext() , "Enter the valid User Credentials" , Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(binding.emailBb.text.toString().trim() , binding.passwordBb.text.toString().trim()).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                logRef.whereEqualTo("user_id", auth.uid).get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            if (document.getString("entry_name") == "Blood Bank") {
                                val intent = Intent(requireContext(), BloodActivity::class.java)
                                startActivity(intent)
                                return@addOnSuccessListener
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            "Enter valid user credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }else{
               Toast.makeText(requireContext() , "Enter the valid User Credentials" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}