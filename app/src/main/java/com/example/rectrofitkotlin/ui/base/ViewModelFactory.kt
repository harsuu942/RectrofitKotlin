package com.example.rectrofitkotlin.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rectrofitkotlin.data.*
import com.example.rectrofitkotlin.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(MainRepository(apiHelper)) as T
            }
        throw IllegalArgumentException("Unknown class name")
    }

}