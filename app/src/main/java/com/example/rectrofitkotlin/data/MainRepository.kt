package com.example.rectrofitkotlin.data

class MainRepository(private val apiHelper: ApiHelper) {
        suspend fun getUsers() = apiHelper.getUsers()
}
