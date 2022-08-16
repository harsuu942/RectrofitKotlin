package com.example.rectrofitkotlin.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.sample_preference_datastore.RecentUsers
import com.example.sample_preference_datastore.User
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream


object UserStore : Serializer<RecentUsers>{
    override val defaultValue: RecentUsers = RecentUsers.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RecentUsers {
        try {
            return RecentUsers.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: RecentUsers, output: OutputStream) =
        t.writeTo(output)


}


