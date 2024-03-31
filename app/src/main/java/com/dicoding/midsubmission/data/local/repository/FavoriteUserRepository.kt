package com.dicoding.midsubmission.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.midsubmission.data.local.db.FavoriteUser
import com.dicoding.midsubmission.data.local.db.FavoriteUserDao
import com.dicoding.midsubmission.data.local.db.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {

    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()
    fun getFavoriteUserByName(username: String): LiveData<FavoriteUser> {
        return mFavoriteUserDao.getFavoriteUserByName(username)
    }
}