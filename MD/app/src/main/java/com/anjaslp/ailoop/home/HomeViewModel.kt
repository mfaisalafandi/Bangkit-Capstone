import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.anjaslp.ailoop.data.UserRepository
import com.anjaslp.ailoop.data.pref.UserModel

//import com.google.firebase.auth.FirebaseAuth

class HomeViewModel(val repository: UserRepository) : ViewModel() {
    fun getEmail(){
        repository.getSession()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
