package com.example.rectrofitkotlin.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rectrofitkotlin.R
import com.example.rectrofitkotlin.model.User


class MainAdapter(private val users: ArrayList<User>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewUserEmail : AppCompatTextView = itemView.findViewById(R.id.textViewUserEmail)
        private val textViewUserName : AppCompatTextView = itemView.findViewById(R.id.textViewUserName)
        private val imageViewAvatar : ImageView = itemView.findViewById(R.id.imageViewAvatar)


        fun bind(user: User) {
            itemView.apply {
                textViewUserName.text = user.userName
                textViewUserEmail.text = user.userEmail
                Glide.with(imageViewAvatar.context)
                    .load(user.image)
                    .placeholder(R.drawable.noimage)
                    .into(imageViewAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false))

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun addUsers(users: List<User>) {
        this.users.apply {
            clear()
            addAll(users)
        }

    }
}