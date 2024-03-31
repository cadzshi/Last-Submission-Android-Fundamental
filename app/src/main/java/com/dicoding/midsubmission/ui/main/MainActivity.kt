package com.dicoding.midsubmission.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.midsubmission.R
import com.dicoding.midsubmission.adapter.UserAdapter
import com.dicoding.midsubmission.data.remote.response.ItemsItem
import com.dicoding.midsubmission.databinding.ActivityMainBinding
import com.dicoding.midsubmission.databinding.ActivitySettingBinding
import com.dicoding.midsubmission.helper.SettingViewModelFactory
import com.dicoding.midsubmission.ui.favorite.FavoriteUserActivity
import com.dicoding.midsubmission.ui.setting.SettingActivity
import com.dicoding.midsubmission.ui.setting.SettingPreferences
import com.dicoding.midsubmission.ui.setting.SettingViewModel
import com.dicoding.midsubmission.ui.setting.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSetting: ActivitySettingBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSetting = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.toString().trim()
                    if (query.isNotEmpty()) {
                        mainViewModel.searchGithubUser(query)
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(textView.windowToken, 0)
                        searchView.hide()
                    }
                    true
                } else {
                    false
                }
            }
        }
        mainViewModel.githubUser.observe(this) { githubUser ->
            setUserData(githubUser)
        }
        mainViewModel.searchGithubUser.observe(this) { searchGithubUser ->
            setUserData(searchGithubUser)
        }
        mainViewModel.showLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                bindingSetting.switchTheme.isChecked = true

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                bindingSetting.switchTheme.isChecked = false
            }
        }
    }

    private fun moveToFavoriteUserActivity(){
        val move = Intent(this@MainActivity, FavoriteUserActivity::class.java)
        startActivity(move)
    }

    private fun moveToSettingActivity(){
        val move = Intent(this@MainActivity, SettingActivity::class.java)
        startActivity(move)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite_user -> {
                moveToFavoriteUserActivity()
                true
            }
            R.id.action_setting -> {
                moveToSettingActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUserData(userData: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(userData)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }




}