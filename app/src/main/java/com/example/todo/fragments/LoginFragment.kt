package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        // Inflate the layout for this fragment
        binding.loginButton.setOnClickListener {
            signInClicked(it)
        }
        binding.registerButton.setOnClickListener {
            signUpClicked(it)
        }
        return binding.root
    }

    fun signInClicked(view: View) {
        val userEmail = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (userEmail.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(userEmail,password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Welcome: ${auth.currentUser?.email.toString()}",Toast.LENGTH_LONG).show()
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }.addOnFailureListener {  exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show() }
        }

    }
    fun signUpClicked(view: View) {
        val userEmail = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (userEmail.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener {
                if (it.isSuccessful){

                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }.addOnFailureListener {  exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show() }
        }
    }


}