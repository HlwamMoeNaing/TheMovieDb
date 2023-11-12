package com.homelandpay.themoviedb.ui.screen

import androidx.lifecycle.ViewModel
import com.homelandpay.themoviedb.data.model.MovieDetails
import com.homelandpay.themoviedb.repo.MovieDetailRepository
import com.homelandpay.themoviedb.uti.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel  @Inject constructor(
    private val repository: MovieDetailRepository
):ViewModel() {

    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails> {
        return repository.getMoviesDetails(movieId)
    }
}