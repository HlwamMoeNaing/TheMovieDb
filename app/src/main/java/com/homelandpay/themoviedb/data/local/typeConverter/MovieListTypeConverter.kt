package com.homelandpay.themoviedb.data.local.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homelandpay.themoviedb.data.model.Genre
import com.homelandpay.themoviedb.data.model.Movie

class MovieListTypeConverter {
    @TypeConverter
    fun toString(genreList: List<Movie>?): String {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun toGenreList(genreListJsonString: String): List<Movie>? {
        val genreListType = object : TypeToken<List<Movie>?>() {}.type
        return Gson().fromJson(genreListJsonString, genreListType)
    }
}