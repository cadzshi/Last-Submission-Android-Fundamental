package com.dicoding.midsubmission.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.midsubmission.data.remote.response.GithubResponse
import com.dicoding.midsubmission.data.remote.response.ItemsItem
import com.dicoding.midsubmission.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _githubUser = MutableLiveData<List<ItemsItem>>()
    val githubUser : LiveData<List<ItemsItem>> = _githubUser

    private val _searchGithubUser = MutableLiveData<List<ItemsItem>>()
    val searchGithubUser : LiveData<List<ItemsItem>> = _searchGithubUser

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    init {
        githubUser()
        searchGithubUser(query = "")
    }

    private fun githubUser() {
        _showLoading.value = true
        val client = ApiConfig.getApiService().getGithubUser(GITHUB_ID)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _githubUser.value = responseBody.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchGithubUser(query: String) {
        _showLoading.value = true
        val client = ApiConfig.getApiService().getGithubUser(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchGithubUser.value = responseBody.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val GITHUB_ID = "Arif"
    }
}