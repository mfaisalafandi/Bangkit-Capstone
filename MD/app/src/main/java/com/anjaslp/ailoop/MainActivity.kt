package com.anjaslp.ailoop

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anjaslp.ailoop.databinding.ActivityMainBinding
import com.anjaslp.ailoop.home.HomeActivity
import com.anjaslp.ailoop.login.LoginActivity
import com.anjaslp.ailoop.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val  mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

//    override fun onStart() {
//        super.onStart()
//        if (mainViewModel.isUserLoggedIn()) {
//            startActivity(Intent(this, HomeActivity::class.java))
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        mainViewModel = MainViewModel(repository)

        mainViewModel.getSession().observe(this){ user ->
            if (user.isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        binding.btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
