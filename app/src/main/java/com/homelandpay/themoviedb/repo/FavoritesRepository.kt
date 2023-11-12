package com.homelandpay.themoviedb.repo

import androidx.lifecycle.LiveData
import com.homelandpay.themoviedb.data.local.Favorite
import com.homelandpay.themoviedb.data.local.FavoritesDatabase
import javax.inject.Inject

class FavoritesRepository @Inject constructor(private val database: FavoritesDatabase){
    suspend fun insertFavorite(favorite: Favorite) {
        database.dao.insertFavorite(favorite)
    }
    fun isFavorite(mediaId: Int): LiveData<Boolean> {
        return database.dao.isFavorite(mediaId)
    }


    fun getFavorites(): LiveData<List<Favorite>> {
        return database.dao.getAllFavorites()
    }

    suspend fun deleteOneFavorite(favorite: Favorite) {
        database.dao.deleteAFavorite(favorite)
    }

    suspend fun deleteAllFavorites() {
        database.dao.deleteAllFavorites()
    }
}