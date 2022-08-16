package com.example.rectrofitkotlin.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.sample_preference_datastore.RecentUsers
import com.example.sample_preference_datastore.User
import kotlinx.coroutines.flow.*
import java.io.IOException

class UserStoreRepository(
    private val context: Context,
    private val protoDataStore: DataStore<RecentUsers>
) : UserStoreRepo {


    override suspend fun getRecentUsers(): Flow<List<User>> {
        return protoDataStore.data.catch { exception ->
            if(exception is IOException){
                emit(RecentUsers.getDefaultInstance())
            }else{
                throw exception
            }
        }.map { protoBuilder ->
            protoBuilder.usersList
        }
    }

    override suspend fun addRecentUser(users: List<User>) {
        protoDataStore.updateData { recentUsers: RecentUsers ->

            if(recentUsers.toBuilder().usersList.containsAll(users)){
                recentUsers
            }else {
                recentUsers.toBuilder().addAllUsers(users).build()
            }
        }
    }

    override suspend fun clearAllRecentUsers() {
        protoDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}

