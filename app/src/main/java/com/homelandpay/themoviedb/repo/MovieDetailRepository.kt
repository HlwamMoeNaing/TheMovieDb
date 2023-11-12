package com.homelandpay.themoviedb.repo

import com.homelandpay.themoviedb.data.TMDBApi
import com.homelandpay.themoviedb.data.model.MovieDetails
import com.homelandpay.themoviedb.uti.Resource
import timber.log.Timber
import javax.inject.Inject

class MovieDetailRepository  @Inject constructor(private val api: TMDBApi) {
    suspend fun getMoviesDetails(movieId: Int): Resource<MovieDetails> {
        val response = try {
            api.getMovieDetails(movieId)
        } catch (e: Exception) {
            return Resource.Error("Unknown error occurred")
        }
        Timber.d("Movie details: $response")
        return Resource.Success(response)
    }
}