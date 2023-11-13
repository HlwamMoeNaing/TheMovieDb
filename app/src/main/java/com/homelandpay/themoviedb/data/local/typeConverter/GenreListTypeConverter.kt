package com.homelandpay.themoviedb.data.local.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homelandpay.themoviedb.data.model.Genre

class GenreListTypeConverter {
    @TypeConverter
    fun toString(genreList: List<Genre>?): String {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun toGenreList(genreListJsonString: String): List<Genre>? {
        val genreListType = object : TypeToken<List<Genre>?>() {}.type
        return Gson().fromJson(genreListJsonString, genreListType)
    }
}