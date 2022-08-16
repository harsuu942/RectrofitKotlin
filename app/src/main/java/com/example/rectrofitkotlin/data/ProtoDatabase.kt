package com.example.rectrofitkotlin.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.rectrofitkotlin.model.UserStore
import com.example.sample_preference_datastore.RecentUsers
import com.example.sample_preference_datastore.User

private val USER_DATA_STORE_FILE_NAME = "user_store.pb"

val Context.userDataStore: DataStore<RecentUsers> by dataStore(
    fileName = USER_DATA_STORE_FILE_NAME,
    serializer = UserStore
)


