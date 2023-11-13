package com.homelandpay.themoviedb.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.homelandpay.themoviedb.data.model.Movie
import java.util.concurrent.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies:List<Movie>)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("DELETE FROM movies")
    suspend fun deleteAllSaveMovies()


    @Query("SELECT * From movies Where category LIKE :mCategory")
    suspend fun getMoviesWithCategory(mCategory: String):List<Movie>

    @Update
    fun updateMovie(entity: Movie)

    @Query("UPDATE movies SET isFavourite = :isFavourite WHERE id = :movieId")
    suspend fun updateFavouriteStatus(movieId: Int, isFavourite: Boolean)

    @Query("SELECT isFavourite FROM movies WHERE id = :mediaId")
    fun isFavorite(mediaId: Int): LiveData<Boolean>

}