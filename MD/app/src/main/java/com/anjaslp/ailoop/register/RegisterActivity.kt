package com.anjaslp.ailoop.register

import RegisterViewModel
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anjaslp.ailoop.ViewModelFactory
import com.anjaslp.ailoop.databinding.ActivityRegisterBinding
import com.anjaslp.ailoop.home.HomeActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch
import com.anjaslp.ailoop.data.Result
import com.anjaslp.ailoop.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressDialog: ProgressDialog
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

//    override fun onStart() {
//        super.onStart()
//        registerViewModel.checkUserLoggedIn()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu...")

//        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

//        registerViewModel.registrationResult.observe(this, { registered ->
//            if (registered) {
//                startActivity(Intent(this, HomeActivity::class.java))
//            }
//        })
//
//        registerViewModel.errorMessage.observe(this, { error ->
//            progressDialog.dismiss()
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//        })
//
//        binding.btSignup.setOnClickListener {
//            if (binding.editName.text.isNotEmpty()
//                && binding.editEmail.text.isNotEmpty()
//                && binding.editPassword.text.isNotEmpty()
//            ) {
//                val fullName = binding.editName.text.toString()
//                val email = binding.editEmail.text.toString()
//                val password = binding.editPassword.text.toString()
//                registerViewModel.register(fullName, email, password)
//                progressDialog.show()
//            } else {
//                Toast.makeText(this, "Silahkan isi dulu semua data", Toast.LENGTH_SHORT).show()
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
        binding.btSignup.setOnClickListener {
            lifecycleScope.launch {
                val name = binding.editName.text.toString().trim()
                val email = binding.editEmail.text.toString().trim()
                val password = binding.editPassword.text.toString().trim()
                registerViewModel.register(name, email, password).observe(this@RegisterActivity) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            AlertDialog.Builder(this@RegisterActivity).apply {
                                setTitle("Register")
                                setMessage("Berhasil membuat akun dengan email $email . Silahkan login")
                                setPositiveButton("Login") { _, _ ->
                                    val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(loginIntent)
                                }
                                create()
                                show()
                            }
                        }

                        is Result.Error -> {
                            AlertDialog.Builder(this@RegisterActivity).apply {
                                setTitle("Register")
                                setMessage("Akun $email gagal dibuat, ${it.message}")
                                setPositiveButton("Ulangi") { _, _ ->
                                    val signIntent = Intent(this@RegisterActivity, RegisterActivity::class.java)
                                    signIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(signIntent)
                                }
                                create()
                                show()
                            }
                        }
                        else->{}
                    }
                }
            }
        }
    }

    companion object {
        var EMAIL_RESULT = false
        var PASSWORD_RESULT = false

    }
}
