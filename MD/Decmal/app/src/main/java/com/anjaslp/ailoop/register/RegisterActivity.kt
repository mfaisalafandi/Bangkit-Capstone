package com.anjaslp.ailoop.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.anjaslp.ailoop.databinding.ActivityRegisterBinding
import com.anjaslp.ailoop.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
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
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu...")

        binding.btSignup.setOnClickListener {
            if (binding.editName.text.isNotEmpty()
                && binding.editEmail.text.isNotEmpty()
                && binding.editPassword.text.isNotEmpty()
            ) {
                //LAUNCH REGISTER
                progressRegister()
            } else {
                Toast.makeText(this, "Silahkan isi dulu semua data", LENGTH_SHORT).show()
            }
        }
    }

    private fun progressRegister() {
        val fullName = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUpdateProfil = userProfileChangeRequest {
                        displayName = fullName
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfil)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        .addOnFailureListener{ error2 ->
                            Toast.makeText(this, error2.localizedMessage, LENGTH_SHORT).show()
                        }
                }else{
                    progressDialog.dismiss()
                //    Toast.makeText(this, "Pendaftaran Gagal!", LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
    }
}