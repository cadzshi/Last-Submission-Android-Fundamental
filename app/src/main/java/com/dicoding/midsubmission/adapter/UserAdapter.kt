package com.dicoding.midsubmission.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.midsubmission.data.remote.response.ItemsItem
import com.dicoding.midsubmission.databinding.UserRowBinding
import com.dicoding.midsubmission.ui.detail.DetailUserActivity
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.KEY_AVATAR
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.KEY_USER
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.KEY_USERNAME

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra(KEY_USER, user.login)

            intentDetail.putExtra(KEY_USERNAME, user.login)
            intentDetail.putExtra(KEY_AVATAR, user.avatarUrl)

            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvUserName.text = user.login
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.civUserPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }




}
