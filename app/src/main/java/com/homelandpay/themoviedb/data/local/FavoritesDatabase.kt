package com.homelandpay.themoviedb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.homelandpay.themoviedb.data.model.Movie

@Database(entities = [Favorite::class,Movie::class], version = 4)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract val dao: FavoritesDao
    abstract val movieDao:MovieDao
}