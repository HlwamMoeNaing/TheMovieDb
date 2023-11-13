package com.homelandpay.themoviedb.data.local.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreIdTypeConverter {
    @TypeConverter
    fun toString(genreList:List<Int>?):String{
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun toGenreId(genreIdJsonString:String):List<Int>?{
        val gereIdListType = object : TypeToken<List<Int>?>() {}.type
        return Gson().fromJson(genreIdJsonString,gereIdListType)

    }
}