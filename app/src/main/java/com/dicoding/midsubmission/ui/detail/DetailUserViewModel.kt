package com.dicoding.midsubmission.ui.detail

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.midsubmission.R
import com.dicoding.midsubmission.data.local.db.FavoriteUser
import com.dicoding.midsubmission.data.local.repository.FavoriteUserRepository
import com.dicoding.midsubmission.data.remote.response.DetailUserResponse
import com.dicoding.midsubmission.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val favoriteUserRepository: FavoriteUserRepository): ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: MutableLiveData<DetailUserResponse?> = _detailUser

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    fun getDetailUser(user: String){
        _showLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(user)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailUser.value = responseBody
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun isFavoriteUserByName(username: String): LiveData <FavoriteUser>{
        return favoriteUserRepository.getFavoriteUserByName(username)
    }
    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserRepository.insert(favoriteUser)
        }
    }
    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserRepository.delete(favoriteUser)
        }
    }

    companion object{
        private const val TAG = "MainActivity"
        const val KEY_USER = "key_user"
        const val KEY_USERNAME = " "
        const val KEY_AVATAR = "key_avatar"

        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}