package com.example.rectrofitkotlin.data

class ApiHelper(private val apiService: ApiService) {

    suspend fun getUsers() = apiService.getUsers()

}