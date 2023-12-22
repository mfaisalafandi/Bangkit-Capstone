package com.anjaslp.ailoop.di

import android.content.Context
import com.anjaslp.ailoop.data.UserRepository
import com.anjaslp.ailoop.data.pref.UserPreference
import com.anjaslp.ailoop.data.remote.ApiConfig


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context)
//        val user = runBlocking { pref.getSession().first() }
        val api = ApiConfig.getApiService(context)
        return UserRepository.getInstance(pref,api)
    }
}