package com.dicoding.midsubmission.ui.favorite

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.midsubmission.R
import com.dicoding.midsubmission.adapter.FavoriteUserAdapter
import com.dicoding.midsubmission.data.local.db.FavoriteUser
import com.dicoding.midsubmission.data.local.repository.FavoriteUserRepository
import com.dicoding.midsubmission.databinding.ActivityFavoriteUserBinding
import com.dicoding.midsubmission.helper.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel
    private lateinit var favoriteUserRepository: FavoriteUserRepository
    private val adapter = FavoriteUserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteUserRepository = FavoriteUserRepository(application)
        favoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteUser.addItemDecoration(itemDecoration)

        val title = getString(R.string.action_bar_favorite_user)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteUserViewModel.showLoading.observe(this){
            showLoading(it)
        }
        favoriteUserViewModel.favoriteUser.observe(this) { users ->
            val items = arrayListOf<FavoriteUser>()
            users.map { favoriteUser ->
                val item = FavoriteUser(username = favoriteUser.username, avatarUrl = favoriteUser.avatarUrl)
                items.add(item)
            }
            adapter.submitList(items)
            binding.rvFavoriteUser.adapter = adapter
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        favoriteUserRepository = FavoriteUserRepository(application)
        val factory = ViewModelFactory.getInstance(favoriteUserRepository)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    private fun showLoading(state: Boolean) { binding.progressBar4.visibility = if (state) View.VISIBLE else View.GONE }
}


