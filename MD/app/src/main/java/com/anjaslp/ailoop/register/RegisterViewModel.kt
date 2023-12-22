import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anjaslp.ailoop.data.UserRepository
import com.anjaslp.ailoop.data.request.RegisterRequest

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.userProfileChangeRequest

class RegisterViewModel(private val repo: UserRepository) : ViewModel() {

//    private val firebaseAuth = FirebaseAuth.getInstance()
//    val registrationResult: MutableLiveData<Boolean> = MutableLiveData()
//    val errorMessage: MutableLiveData<String> = MutableLiveData()
//
//    fun checkUserLoggedIn() {
//        if (firebaseAuth.currentUser != null) {
//            registrationResult.postValue(true)
//        }
//    }

//    fun register(fullName: String, email: String, password: String) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val userUpdateProfile = userProfileChangeRequest {
//                        displayName = fullName
//                    }
//                    val user = task.result?.user
//                    user?.updateProfile(userUpdateProfile)
//                        ?.addOnCompleteListener {
//                            registrationResult.postValue(true)
//                        }
//                        ?.addOnFailureListener { error2 ->
//                            errorMessage.postValue(error2.localizedMessage)
//                        }
//                } else {
//                    errorMessage.postValue(task.exception?.localizedMessage)
//                }
//            }
//            .addOnFailureListener { error ->
//                errorMessage.postValue(error.localizedMessage)
//            }
//    }

    suspend fun register(name: String, email: String, password: String) = repo.register(RegisterRequest( name, email, password))

}
