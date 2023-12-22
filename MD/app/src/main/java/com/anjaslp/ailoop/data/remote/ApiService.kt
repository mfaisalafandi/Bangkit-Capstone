package com.anjaslp.ailoop.data.remote

import com.anjaslp.ailoop.data.request.LoginRequest
import com.anjaslp.ailoop.data.request.RegisterRequest
import com.anjaslp.ailoop.data.response.LoginResponse
import com.anjaslp.ailoop.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
//    @FormUrlEncoded
    @POST("user/signup")
    suspend fun register(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("user/signin")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}