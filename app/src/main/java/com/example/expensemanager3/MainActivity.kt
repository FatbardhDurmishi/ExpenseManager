package com.example.expensemanager3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.expensemanager3.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth=FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.logInButton.setOnClickListener {
            val email = binding.emailLoginInput.text.toString()
            val pass = binding.passwordLoginInput.text.toString()

            if (email.isEmpty()) {
                binding.emailLoginInput.error = "Please enter a valid email"
            }
            if (pass.isEmpty()) {
                binding.passwordLoginInput.error = "Please enter a password"
            }

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(pass.length>=8){
                        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        binding.passwordLoginInput.error ="Password should be at least 8 characters"
                    }
                }else{
                    binding.emailLoginInput.error="Please enter a valid email"
                }

            }else{
                Toast.makeText(this,"No empty fields allowed", Toast.LENGTH_SHORT)
                    .show()            }
        }

        binding.createAccount.setOnClickListener {
            val intent = Intent(this,SingUpActivity::class.java)
            startActivity(intent)
        }
    }
}