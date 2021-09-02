package com.example.myappproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var createprofileButton: Button
    private lateinit var profileButton : Button
    private lateinit var postButton : Button

    private lateinit var menuImageButton: ImageButton
    private lateinit var  recyclerView: RecyclerView
    private lateinit var floatingActionButton : FloatingActionButton
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logoutButton = findViewById(R.id.btn_profilelogout)
        createprofileButton = findViewById(R.id.btn_createprofilebotton)
        profileButton = findViewById(R.id.btn_profilebotton)
        postButton = findViewById(R.id.btn_post)

        auth = Firebase.auth
        database = Firebase.database
        storage = Firebase.storage

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        createprofileButton.setOnClickListener {
            val intent = Intent(this, CreateProfile::class.java)
            startActivity(intent)
        }
         profileButton.setOnClickListener {
             val intent = Intent(this, ProfileActivity::class.java)
             startActivity(intent)
         }

        postButton.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
    }
}