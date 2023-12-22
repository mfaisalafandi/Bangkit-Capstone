package com.anjaslp.ailoop.camera

import CameraViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anjaslp.ailoop.R
import com.anjaslp.ailoop.databinding.ActivityCameraBinding
import com.anjaslp.ailoop.home.HomeActivity
import com.anjaslp.ailoop.ml.ConvertedModel
import com.anjaslp.ailoop.profile.ProfileActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraViewModel: CameraViewModel
    private val imageSize = 224
    private var backButtonPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)

        cameraViewModel.cameraPermissionGranted.observe(this) { permissionGranted ->
            binding.btTake.isEnabled = permissionGranted
            binding.btGallery.isEnabled = permissionGranted
        }

        cameraViewModel.imageResult.observe(this) { imageBitmap ->
            binding.imageResult.setImageBitmap(imageBitmap)
        }

        binding.btTake.setOnClickListener {
            cameraViewModel.takePhoto(this)
        }

        binding.btGallery.setOnClickListener {
            cameraViewModel.pickFromGallery(this)
        }

        binding.btPredict.setOnClickListener {
            val imageBitmap = cameraViewModel.getImageBitmap()
            if (imageBitmap != null) {
                classifyImage(imageBitmap)
            }else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }

        setupBottomAppBar()
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val model = ConvertedModel.newInstance(applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++]
                    byteBuffer.putFloat(((`val` shr 16 and 0xFF) * (1f / 255f)).toFloat())
                    byteBuffer.putFloat(((`val` shr 8 and 0xFF) * (1f / 255f)).toFloat())
                    byteBuffer.putFloat((`val` and (0xFF * (1f / 255f)).toInt()).toFloat())
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.getOutputFeature0AsTensorBuffer()

            val confidence = outputFeature0.floatArray

            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidence.indices) {
                if (confidence[i] > maxConfidence) {
                    maxConfidence = confidence[i]
                    maxPos = i
                }
            }

            val classes = arrayOf("No Malaria Detected", "Malaria detected")
            binding.tvResult.text = classes[maxPos]

            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
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

    override fun onStart() {
        super.onStart()
        cameraViewModel.checkCameraPermission(this)
        requestPermissions()
    }

    private fun requestPermissions() {
        val cameraPermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        val storagePermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        if (!cameraPermission || !storagePermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                111
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111) {
            cameraViewModel.checkCameraPermission(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cameraViewModel.handleActivityResult(requestCode, resultCode, data, this)
    }

    private fun setupBottomAppBar() {
        val btnHome = findViewById<LinearLayout>(R.id.btnHome)
        val btnProfile = findViewById<LinearLayout>(R.id.btnProfile)

        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}