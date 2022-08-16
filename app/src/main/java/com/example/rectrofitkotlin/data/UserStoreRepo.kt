package com.example.rectrofitkotlin.data

import androidx.lifecycle.LiveData
import com.example.sample_preference_datastore.User
import kotlinx.coroutines.flow.Flow

interface UserStoreRepo {
    suspend fun getRecentUsers(): Flow<List<User>>
    suspend fun addRecentUser(users: List<User>)
    suspend fun clearAllRecentUsers()
}