package com.example.myappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.emaildua)
        passwordEditText = findViewById(R.id.passworddua)
        loginButton = findViewById(R.id.btn_loginsatu)
        registerButton = findViewById(R.id.btn_registerdua)
        auth = Firebase.auth

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        val error = it.exception
                        Toast.makeText(this, "Error : ${error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
