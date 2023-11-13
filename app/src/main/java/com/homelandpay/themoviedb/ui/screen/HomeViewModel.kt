package com.homelandpay.themoviedb.ui.screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.homelandpay.themoviedb.data.local.FavoritesDatabase

import com.homelandpay.themoviedb.data.model.Movie
import com.homelandpay.themoviedb.repo.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val database: FavoritesDatabase
) : ViewModel() {
    private val _popularMovies = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val popularMovies: State<Flow<PagingData<Movie>>> = _popularMovies

    private val _upcomingMovies = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val upcomingMovies: State<Flow<PagingData<Movie>>> = _upcomingMovies


    init {
        getPopularMovies(null)
        getUpcomingMovies(null)
        getTestData()
        getAndSaveUpcomingMovie()
    }

    fun getPopularMovies(genreId: Int?) {
        viewModelScope.launch {
            _popularMovies.value = if (genreId != null) {
                moviesRepository.getPopularMovies().map { pagingData ->
                    pagingData.filter {
                        it.genreIds.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                moviesRepository.getPopularMovies().cachedIn(viewModelScope)
            }
        }
    }

    fun getUpcomingMovies(genreId: Int?) {
        viewModelScope.launch {
            _upcomingMovies.value = if (genreId != null) {
                moviesRepository.getUpcomingMovies().map { pagingData ->
                    pagingData.filter {
                        it.genreIds.contains(genreId)
                    }
                }.cachedIn(viewModelScope)
            } else {
                moviesRepository.getUpcomingMovies().cachedIn(viewModelScope)
            }
        }
    }

    fun getTestData() {
        viewModelScope.launch {
            moviesRepository.getTestData()
                .onStart {
                    Log.d("@VmStatus", "Loading")

                }.catch {
                    Log.d("@VmStatus", "catch -> ${it.printStackTrace()}")

                }.collectLatest {
                    Log.d("@VmStatus", "response -> ${it.body().toString()}")
                    val responseState = it.isSuccessful
                    if (responseState) {
                        val successBody = it.body()
                        successBody?.let { resp ->
                            val mMovieList = ArrayList<Movie>()
                            val movieList = resp.searches
                            for (m in movieList) {
                                val model = m.copy(
                                    category = "Popular"
                                )
                                mMovieList.add(model)
                            }

                            database.movieDao.insertMovies(mMovieList)
                        }
                    } else {
                        val successBody = it.errorBody()

                    }

                }
        }
    }


    fun getAndSaveUpcomingMovie() {
        viewModelScope.launch {
            moviesRepository.getTestUpcomingMovie()
                .onStart {
                    Log.d("@VmUpcom", "Loading")

                }.catch {
                    Log.d("@VmUpcom", "catch -> ${it.printStackTrace()}")

                }.collectLatest {
                    Log.d("@VmUpcom", "response -> ${it.body().toString()}")
                    val responseState = it.isSuccessful
                    if (responseState) {
                        val successBody = it.body()
                        successBody?.let { resp ->
                            //deleteAllSaveMovies()

                            val mMovieList = ArrayList<Movie>()
                            val movieList = resp.searches
                            for (m in movieList) {
                                val model = m.copy(
                                    category = "Upcoming"
                                )
                                mMovieList.add(model)
                            }
                            database.movieDao.insertMovies(mMovieList)
                        }
                    } else {
                        val successBody = it.errorBody()

                    }

                }
        }
    }

    val getTestMovies = moviesRepository.getAllMovies()


    fun deleteAllSaveMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.deleteAllSaveMovies()
        }
    }

    fun updateMovie(entity: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.updateMovie(entity)
        }

    }

    fun onFavouriteIconClicked(movieId: Int, currentFavouriteStatus: Boolean) {
        viewModelScope.launch {
            database.movieDao
                .updateFavouriteStatus(movieId, !currentFavouriteStatus)
        }
    }


    val popularList = ArrayList<Movie>()
    fun mGetPopular():List<Movie>{
        viewModelScope.launch {
            moviesRepository.getMoviesWithCategory("Popular")
               .collectLatest {
                   popularList.addAll(it)
               }

        }
        return popularList ?: emptyList()

    }


    val upcomingList= ArrayList<Movie>()
    fun mGetUpcoming():List<Movie>{
        viewModelScope.launch {
            moviesRepository.getMoviesWithCategory("Upcoming")
                .collectLatest {
                    upcomingList.addAll(it)
                }

        }
     return upcomingList

    }

    fun isAFavorite(id: Int): LiveData<Boolean> {
        return moviesRepository.isFavorite(id)
    }


}