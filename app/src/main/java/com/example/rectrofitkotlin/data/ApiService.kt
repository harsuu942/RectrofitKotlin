package com.example.rectrofitkotlin.data

import com.example.rectrofitkotlin.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>


}