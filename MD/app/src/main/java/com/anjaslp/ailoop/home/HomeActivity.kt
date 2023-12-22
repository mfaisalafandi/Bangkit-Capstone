package com.anjaslp.ailoop.home

import HomeViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.anjaslp.ailoop.camera.CameraActivity
import com.anjaslp.ailoop.profile.ProfileActivity
import com.anjaslp.ailoop.R
import com.anjaslp.ailoop.ViewModelFactory
import com.anjaslp.ailoop.databinding.ActivityHomeBinding
import com.anjaslp.ailoop.login.LoginActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var textFullName: TextView
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var backButtonPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.getSession().observe(this) { session ->
            if (session.isLogin) {
                val greetingText = "Hi, ${session.name}"
                binding.fullName.text = greetingText
            }
        }

        setupBottomAppBar()
        imageAdapter()
    }

    override fun onBackPressed() {
        if (backButtonPressedOnce) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backButtonPressedOnce = true

            android.os.Handler().postDelayed({
                backButtonPressedOnce = false
            }, 2000)
        }
    }

    private fun setupBottomAppBar() {
        val btnCamera = findViewById<LinearLayout>(R.id.btnCamera)
        val btnProfile = findViewById<LinearLayout>(R.id.btnProfile)

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