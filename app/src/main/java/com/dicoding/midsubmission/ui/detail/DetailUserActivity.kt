package com.dicoding.midsubmission.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.midsubmission.R
import com.dicoding.midsubmission.adapter.SectionsPagerAdapter
import com.dicoding.midsubmission.data.local.db.FavoriteUser
import com.dicoding.midsubmission.data.local.repository.FavoriteUserRepository
import com.dicoding.midsubmission.data.remote.response.DetailUserResponse
import com.dicoding.midsubmission.databinding.ActivityDetailUserBinding
import com.dicoding.midsubmission.helper.ViewModelFactory
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.KEY_AVATAR
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.KEY_USER
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.KEY_USERNAME
import com.dicoding.midsubmission.ui.detail.DetailUserViewModel.Companion.TAB_TITLES
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var favoriteUserRepository: FavoriteUserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteUserRepository = FavoriteUserRepository(application)
        detailUserViewModel = obtainViewModel(this@DetailUserActivity)


        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getStringExtra(KEY_USER)
        } else {
            intent.getStringExtra(KEY_USER)
        }
        val username = intent.getStringExtra(KEY_USERNAME).toString()
        val avatarUrl = intent.getStringExtra(KEY_AVATAR).toString()
        val favoriteUserLiveData = detailUserViewModel.isFavoriteUserByName(username)

        if (user != null){
            detailUserViewModel.getDetailUser(user)
        }
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            userData(detailUser)
        }
        detailUserViewModel.showLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.toString()

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        supportActionBar?.title = user
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteUserLiveData.observe(this) {favoriteUser ->
            if (favoriteUser != null){
                binding.fabFavoriteBorder.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.fabFavoriteBorder.setImageResource(R.drawable.ic_favorite_border)
            }
        }
        binding.fabFavoriteBorder.setOnClickListener {
            if (favoriteUserLiveData.value == null){
                val favoriteUser = FavoriteUser(username = username, avatarUrl = avatarUrl)
                detailUserViewModel.insertFavoriteUser(favoriteUser)
                Toast.makeText(this, "Menambahkan ${username} ke list Favorite", Toast.LENGTH_SHORT).show()
            } else {
                favoriteUserLiveData.value?.let { favoriteUser ->
                    detailUserViewModel.deleteFavoriteUser(favoriteUser)
                    Toast.makeText(this, "Menghapus ${username} dari list Favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun userData(value: DetailUserResponse?){
        with(binding){
            tvUserName.text = value?.login
            tvNickName.text = value?.name
            Glide.with(this@DetailUserActivity)
                .load(value?.avatarUrl)
                .into(civUserPhoto)
            tvFollowing.text = resources.getString(R.string.total_following, value?.following)
            tvFollower.text =  resources.getString(R.string.total_followers, value?.followers)
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

    private fun showLoading(state: Boolean) { binding.progressBar3.visibility = if (state) View.VISIBLE else View.GONE }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        favoriteUserRepository = FavoriteUserRepository(application)
        val factory = ViewModelFactory.getInstance(favoriteUserRepository)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

}