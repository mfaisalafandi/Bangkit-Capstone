package com.anjaslp.ailoop

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.anjaslp.ailoop.data.UserRepository
import com.anjaslp.ailoop.data.pref.UserModel
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainViewModel( private val repository: UserRepository) : ViewModel() {
//    private val firebaseAuth = FirebaseAuth.getInstance()

//    fun isUserLoggedIn(): Boolean {
//        return firebaseAuth.currentUser != null
//    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


}