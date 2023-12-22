import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anjaslp.ailoop.data.UserRepository
import com.anjaslp.ailoop.data.pref.UserModel
import com.anjaslp.ailoop.data.request.LoginRequest
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

//    private val firebaseAuth = FirebaseAuth.getInstance()
//    val loginResult: MutableLiveData<Boolean> = MutableLiveData()
//    val errorMessage: MutableLiveData<String> = MutableLiveData()
//
//    fun checkUserLoggedIn() {
//        if (firebaseAuth.currentUser != null) {
//            loginResult.postValue(true)
//        }
//    }

//    fun login(email: String, password: String) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//            .addOnSuccessListener {
//                loginResult.postValue(true)
//            }
//            .addOnFailureListener { error ->
//                errorMessage.postValue(error.localizedMessage)
//            }
//    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    suspend fun login(email: String, password: String) = repository.login(LoginRequest(email, password))

}
