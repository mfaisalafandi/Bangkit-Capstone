package com.anjaslp.ailoop.login

import LoginViewModel
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anjaslp.ailoop.MainActivity
import com.anjaslp.ailoop.R
import com.anjaslp.ailoop.ViewModelFactory
import com.anjaslp.ailoop.databinding.ActivityLoginBinding
import com.anjaslp.ailoop.databinding.ActivityMainBinding
import com.anjaslp.ailoop.databinding.ActivityRegisterBinding
import com.anjaslp.ailoop.home.HomeActivity
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.anjaslp.ailoop.data.Result
import com.anjaslp.ailoop.data.pref.UserModel
import com.anjaslp.ailoop.data.response.LoginResult

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu...")

//        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

//        loginViewModel.loginResult.observe(this, { loggedIn ->
//            if (loggedIn) {
//                startActivity(Intent(this, HomeActivity::class.java))
//            }
//        })

//        loginViewModel.errorMessage.observe(this, { error ->
//            progressDialog.dismiss()
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//        })

//        binding.btLogin.setOnClickListener {
//            if (binding.editEmail.text.isNotEmpty() && binding.editPassword.text.isNotEmpty()) {
//                val email = binding.editEmail.text.toString()
//                val password = binding.editPassword.text.toString()
//                loginViewModel.login(email, password)
//                progressDialog.show()
//            } else {
//                Toast.makeText(this, "Silahkan isi Email dan Password terlebih dahulu", Toast.LENGTH_SHORT).show()
//            }
//        }
        setupView()
        setupAction()

    }



    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {

        binding.btLogin.setOnClickListener {
            lifecycleScope.launch {
                val email = binding.editEmail.text.toString().trim()
                val password = binding.editPassword.text.toString().trim()
                loginViewModel.login(email, password).observe(this@LoginActivity) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            it.data?.let { loginResult ->
                                loginResult.loginResult?.token?.let { token ->
                                    emailSuccess= email
                                    loginViewModel.saveSession(UserModel(loginResult.loginResult.name!!, email, token, true))
                                    AlertDialog.Builder(this@LoginActivity).apply {
                                        setTitle("Login")
                                        setMessage("Berhasil Login, selamat datang $email")
                                        setPositiveButton("Masuk") { _, _ ->
                                            val loginIntent = Intent(context, MainActivity::class.java)
                                            loginIntent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(loginIntent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                            }
                        }

                        is Result.Error -> {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Login")
                                setMessage("Periksa kembali email dan password anda")
                                setPositiveButton("Ulangi") { _, _ ->
                                    val loginIntent =
                                        Intent(this@LoginActivity, LoginActivity::class.java)
                                    loginIntent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(loginIntent)
                                }
                                create()
                                show()
                            }
                        }

                        else -> {
                            // Handle other cases as needed
                        }
                    }
                }
            }
        }

    }

    companion object{
        var emailSuccess = "email"
    }
}
