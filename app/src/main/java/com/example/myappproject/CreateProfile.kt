package com.example.myappproject

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class CreateProfile : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameedittext: EditText
    private lateinit var bioedittext : EditText
    private lateinit var emailedittext : EditText
    private lateinit var createbutton: Button
    private lateinit var database : FirebaseDatabase
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth

    private var imageUri: Uri? = null
    private var uid: String? = null

    private lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        database = Firebase.database
        firestore = Firebase.firestore
        storage = Firebase.storage
        auth = Firebase.auth

        profile = Profile()

        imageView = findViewById(R.id.createProfileImageView)
        nameedittext = findViewById(R.id.createprofilenameedittext)
        bioedittext = findViewById(R.id.createprofilebioedittext)
        emailedittext = findViewById(R.id.createprofileemailedittext)
        createbutton = findViewById(R.id.createprofilebuton)


        val currentUser = auth.currentUser
        currentUser?.let {
            uid = it.uid
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                if (it?.resultCode == Activity.RESULT_OK) {
                    it.data?.let {
                        imageUri = it.data
                        Glide.with(this).load(imageUri).into(imageView)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        imageView.setOnClickListener {
            val intent = Intent().apply {
                setType("image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            resultLauncher.launch(intent)
        }

        createbutton.setOnClickListener {
            val name = nameedittext.text.toString()
            val bio = bioedittext.text.toString()
            val email = emailedittext.text.toString()

            if (name.isNotEmpty() || bio.isNotEmpty() || email.isNotEmpty()) {

                imageUri?.let {
                    val contentResolver: ContentResolver = getContentResolver()
                    val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
                    val fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(it))

                    val reference = storage.getReference("profile_images").child(System.currentTimeMillis().toString() + ".${fileExtension}")
                    val uploadTask = reference.putFile(it)
                    uploadTask.continueWithTask {
                        if (!it.isSuccessful) {
                            throw it.exception!!.cause!!
                        }

                        reference.downloadUrl
                    }.addOnCompleteListener {
                        if (it.isSuccessful) {
                            it.result?.let {
                                val downloadUri = it
                                val profileMap = HashMap<String, String>()
                                profileMap.put("name", name)
                                profileMap.put("bio", bio)
                                profileMap.put("email", email)
                                profileMap.put("url", downloadUri.toString())
                                profileMap.put("uid", uid!!)

                                profile.name = name
                                profile.url = downloadUri.toString()
                                profile.uid = uid

                                uid?.let {
                                    database.getReference("users").child(it).setValue(profile)
                                    firestore.collection("users").document(it).set(profileMap).addOnSuccessListener {
                                        Toast.makeText(this, "Profile Created", Toast.LENGTH_SHORT).show()

                                        val handler = Handler(Looper.getMainLooper())
                                        handler.postDelayed({
                                            val intent = Intent(this, ProfileActivity::class.java)
                                            startActivity(intent)
                                        }, 2000)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }


    }
}