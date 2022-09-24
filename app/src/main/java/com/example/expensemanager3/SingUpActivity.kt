package com.example.expensemanager3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.expensemanager3.databinding.ActivitySingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SingUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        binding.btnCreateAcc.setOnClickListener {
            val email = binding.emailSingupInput.text.toString()
            val pass = binding.passwordSingupInput.text.toString()
            val confirmPass = binding.passwordConfirmInput.text.toString()

            if (email.isEmpty()) {
                binding.emailSingupInput.error = "Please enter an email"
            }
            if (pass.isEmpty()) {
                binding.passwordSingupInput.error = "Please enter a password"
            }
            if (confirmPass.isEmpty()) {
                binding.passwordConfirmInput.error = "Please confirm your password"
            }

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (pass.length >= 8) {
                        if (pass == confirmPass) {
                            auth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(this) {
                                    if (it.isSuccessful) {
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this,
                                            it.exception.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                        } else {
                            binding.passwordConfirmInput.error = "Password don't match"
                            binding.passwordSingupInput.error = "Password don't match"
                        }
                    } else {
                        binding.passwordSingupInput.error =
                            "Password should be at least 8 characters"
                    }

                } else {
                    binding.emailSingupInput.error = "Please enter a valid email"
                }

            } else {
                Toast.makeText(this, "No empty fields allowed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.login.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}