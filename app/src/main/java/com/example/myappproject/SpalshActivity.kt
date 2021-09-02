package com.example.myappproject

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SpalshActivity : AppCompatActivity() {
    private lateinit var logoImageView: ImageView
    private lateinit var auth : FirebaseAuth
    private val animationTime: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        auth = Firebase.auth
        logoImageView = findViewById(R.id.splashlogoimageview)

        val animatorY = ObjectAnimator.ofFloat(logoImageView, "y", 400f)

        animatorY.setDuration(animationTime)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorY)
        animatorSet.start()

    }
    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }, 4000)
    }

    }
