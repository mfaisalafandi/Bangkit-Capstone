package com.anjaslp.ailoop.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anjaslp.ailoop.CameraActivity
import com.anjaslp.ailoop.ProfileActivity
import com.anjaslp.ailoop.R
import com.anjaslp.ailoop.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var textFullName: TextView
    private lateinit var textEmail: TextView

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        textFullName = findViewById(R.id.fullName)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!=null){
            textFullName.text = firebaseUser.displayName
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupBottomAppBar()
        imageAdapter()
    }

    private fun setupBottomAppBar() {
        val btnHome = findViewById<LinearLayout>(R.id.btnHome)
        val btnCamera = findViewById<LinearLayout>(R.id.btnCamera)
        val btnProfile = findViewById<LinearLayout>(R.id.btnProfile)

        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun imageAdapter(){

        val images = listOf<Int>(
            R.drawable.malaria,
            R.drawable.malaria1,
            R.drawable.malaria2
        )

        val imageAdapter = ImageAdapter(images)
        findViewById<RecyclerView>(R.id.rvImage).adapter = imageAdapter
    }
}