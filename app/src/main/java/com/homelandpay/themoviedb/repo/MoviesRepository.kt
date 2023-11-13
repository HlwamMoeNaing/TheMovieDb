package com.homelandpay.themoviedb.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.homelandpay.themoviedb.data.TMDBApi
import com.homelandpay.themoviedb.data.local.FavoritesDatabase
import com.homelandpay.themoviedb.data.model.Movie
import com.homelandpay.themoviedb.data.model.MoviesResponse
import com.homelandpay.themoviedb.paging.PopularMoviesSource
import com.homelandpay.themoviedb.paging.UpcomingMoviesSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val api: TMDBApi,
    private val database: FavoritesDatabase
    ) {

    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 27),
            pagingSourceFactory = {
                PopularMoviesSource(api)
            }
        ).flow
    }

    fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 27),
            pagingSourceFactory = {
                UpcomingMoviesSource(api)
            }
        ).flow
    }


    fun getTestData():Flow<Response<MoviesResponse>> =
        flow {
          emit(api.getPopularMoviesTest())
        }.flowOn(Dispatchers.IO)

    fun getTestUpcomingMovie():Flow<Response<MoviesResponse>> =
        flow {
            emit(api.getUpcomingMoviesTest())
        }.flowOn(Dispatchers.IO)

    fun getAllMovies(): LiveData<List<Movie>> {
        return database.movieDao.getAllMovies()
    }
    suspend fun deleteAllSaveMovies(){
        return database.movieDao.deleteAllSaveMovies()
    }
    fun isFavorite(id: Int): LiveData<Boolean> {
        return database.movieDao.isFavorite(id)
    }
fun getMoviesWithCategory(category:String): Flow<List<Movie>> =
    flow {
        emit(database.movieDao.getMoviesWithCategory(category))
    }.flowOn(Dispatchers.IO)


    fun updateMovie(entity:Movie){
        database.movieDao.updateMovie(entity)
    }




}