package com.anjaslp.ailoop.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.anjaslp.ailoop.R
import com.anjaslp.ailoop.databinding.ActivityLoginBinding
import com.anjaslp.ailoop.databinding.ActivityMainBinding
import com.anjaslp.ailoop.databinding.ActivityRegisterBinding
import com.anjaslp.ailoop.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu...")

        binding.btLogin.setOnClickListener {
            if (binding.editEmail.text.isNotEmpty() && binding.editPassword.text.isNotEmpty()) {
                processLogin()
            }else{
                Toast.makeText(this, "Silahkan isi Email dan Password terlebih dahulu", LENGTH_SHORT).show()
            }
        }
    }

    private fun processLogin(){
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            .addOnFailureListener{ error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener{
                progressDialog.dismiss()
            }
    }
}