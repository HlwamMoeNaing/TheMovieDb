package com.homelandpay.themoviedb.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.homelandpay.themoviedb.data.TMDBApi
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

class MoviesRepository @Inject constructor(private val api: TMDBApi) {

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

}