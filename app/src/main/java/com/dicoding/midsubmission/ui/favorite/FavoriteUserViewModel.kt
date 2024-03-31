package com.dicoding.midsubmission.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.midsubmission.data.local.db.FavoriteUser
import com.dicoding.midsubmission.data.local.repository.FavoriteUserRepository
import kotlinx.coroutines.launch

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository) : ViewModel() {

    private val _favoriteUser = favoriteUserRepository.getAllFavoriteUser()
    val favoriteUser: LiveData<List<FavoriteUser>> = _favoriteUser

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    init {
        getFavoriteUser()
    }

    private fun getFavoriteUser(){
        _showLoading.value = true
        viewModelScope.launch{
            try {
                _showLoading.value = false
                favoriteUserRepository.getAllFavoriteUser()
            } catch(e: Exception) {
                _showLoading.value = true
                Log.d(TAG, "Gagal Mendapatkan Data: ${e.message}")
            }
        }

    }
    companion object {
        private const val TAG = "FavoriteUserViewModel"
    }

}