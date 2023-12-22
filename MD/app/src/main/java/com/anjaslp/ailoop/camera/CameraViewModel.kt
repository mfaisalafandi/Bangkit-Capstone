import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException

class CameraViewModel : ViewModel() {

    val imageResult: MutableLiveData<Bitmap?> = MutableLiveData()
    val cameraPermissionGranted: MutableLiveData<Boolean> = MutableLiveData()

    fun getImageBitmap(): Bitmap? {
        return imageResult.value
    }

    fun checkCameraPermission(activity: AppCompatActivity) {
        val cameraPermission =
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        val permissionGranted = cameraPermission
        cameraPermissionGranted.postValue(permissionGranted)
    }

    fun takePhoto(activity: AppCompatActivity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, 101)
        }
    }

    fun pickFromGallery(activity: AppCompatActivity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, 102)
    }

    fun handleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: AppCompatActivity
    ) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                101 -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageResult.postValue(imageBitmap)
                }
                102 -> {
                    try {
                        val selectedImage = data?.data
                        val imageBitmap =
                            MediaStore.Images.Media.getBitmap(
                                activity.contentResolver,
                                selectedImage
                            )
                        imageResult.postValue(imageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
