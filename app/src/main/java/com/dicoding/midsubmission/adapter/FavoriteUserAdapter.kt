package com.dicoding.midsubmission.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.midsubmission.data.local.db.FavoriteUser
import com.dicoding.midsubmission.databinding.UserRowBinding
import com.dicoding.midsubmission.ui.detail.DetailUserActivity
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel

class FavoriteUserAdapter: ListAdapter<FavoriteUser, FavoriteUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra(DetailUserViewModel.KEY_USER, user.username)

            intentDetail.putExtra(DetailUserViewModel.KEY_USERNAME, user.username)
            intentDetail.putExtra(DetailUserViewModel.KEY_AVATAR, user.avatarUrl)

            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            binding.tvUserName.text = favoriteUser.username
            Glide.with(itemView.context)
                .load(favoriteUser.avatarUrl)
                .into(binding.civUserPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}