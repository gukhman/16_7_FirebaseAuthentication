package com.example.firebaseauthentication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.firebaseauthentication.R
import com.example.firebaseauthentication.databinding.FragmentMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.signInBTN.setOnClickListener {
            signUpUser()
        }
        binding.redirectLogInTV.setOnClickListener {
            view
                .findNavController()
                .navigate(
                    R.id.action_mainFragment_to_logInFragment
                )
        }
    }

    private fun signUpUser() {
        val email = binding.emailSignUpET.text.toString()
        val pass = binding.passwordSignUpET.text.toString()
        val confimPass = binding.passwordConfirmSignUpET.text.toString()
        if (email.isBlank() || pass.isBlank() || confimPass.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Все поля должны быть заполнены",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (pass != confimPass) {
            Toast.makeText(
                requireContext(),
                "Пароли не совпадают",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Успешно зарегистрирован",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (auth.currentUser != null) {
                    Toast.makeText(
                        requireContext(),
                        "Пользователь уже существует",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireView()
                        .findNavController()
                        .navigate(
                            R.id.action_mainFragment_to_logInFragment
                        )
                }
                Toast.makeText(
                    requireContext(),
                    "Регистрация не прошла",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}