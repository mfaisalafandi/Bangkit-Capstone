package com.anjaslp.ailoop.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.anjaslp.ailoop.data.pref.UserModel
import com.anjaslp.ailoop.data.pref.UserPreference
import com.anjaslp.ailoop.data.remote.ApiService
import com.anjaslp.ailoop.data.request.LoginRequest
import com.anjaslp.ailoop.data.request.RegisterRequest
import com.anjaslp.ailoop.data.response.LoginResponse
import com.anjaslp.ailoop.data.response.LoginResult
import com.anjaslp.ailoop.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getName():LoginResult{
        return LoginResult()
    }





    suspend fun register(registerRequest: RegisterRequest) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(registerRequest )
            emit(Result.Success(response))
        }
        catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Log.e("ErrorBody", jsonInString ?: "Empty Error Body")
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            emit(errorBody.message?.let { Result.Error(it) })
        }
    }

    suspend fun login(loginRequest: LoginRequest) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginRequest)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(errorBody.message?.let { Result.Error(it) })
        }
    }



    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService)
            }.also { instance = it }
    }
}