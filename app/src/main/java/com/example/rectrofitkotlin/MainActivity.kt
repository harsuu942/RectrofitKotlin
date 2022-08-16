package com.example.rectrofitkotlin

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rectrofitkotlin.data.*
import com.example.rectrofitkotlin.model.User
import com.example.rectrofitkotlin.ui.base.ViewModelFactory
import com.example.rectrofitkotlin.ui.main.adapter.MainAdapter
import com.example.rectrofitkotlin.ui.main.viewmodel.MainViewModel
import com.example.rectrofitkotlin.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit  var progressBar : ProgressBar
    private var userRepo : UserStoreRepo?=null
    private var offlineUsers : ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupUI()
        setupViewModel()
        if(isNetworkAvailable(this)) {
            setupObservers()
        }

    }

    private fun setOfflineUI() {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        GlobalScope.launch(Dispatchers.Main) {
            userRepo?.getRecentUsers()?.collectIndexed { index, value ->
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO){
                   for(i in value.indices){
                       val user = User(image = value[i].image,
                       userEmail = value[i].userEmail,
                       userId = value[i].userId,
                       userName = value[i].userName)
                       offlineUsers.add(user)
                   }
                    adapter.apply {
                        addUsers(offlineUsers)
                        runOnUiThread(Runnable {
                            kotlin.run {
                                notifyDataSetChanged()
                            }
                        })
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        userRepo = UserStoreRepository(this,userDataStore)
        if(isNetworkAvailable(this)) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(
                    ApiHelper(RetrofitBuilder.apiService)
                )
            ).get(MainViewModel::class.java)
        }else{
            setOfflineUI()
        }

    }

    private fun setupUI() {

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let {
                                users -> retrieveList(users)
                        }
                    }

                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<User>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (i in users.indices){
                userRepo?.addRecentUser(
                    listOf(
                        com.example.sample_preference_datastore.User.newBuilder()
                            .setImage(users[i].image)
                            .setUserEmail(users[i].userEmail)
                            .setUserId(users[i].userId)
                            .setUserName(users[i].userName)
                            .build()
                    )
                )

            }

        }

        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }

}

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    return false
}