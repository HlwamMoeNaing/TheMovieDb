package com.homelandpay.themoviedb.ui.screen

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.homelandpay.themoviedb.appComponent.MovieItem
import com.homelandpay.themoviedb.appComponent.MovieItemNew
import com.homelandpay.themoviedb.appNav.Routes

import com.homelandpay.themoviedb.data.model.Movie
import com.homelandpay.themoviedb.ui.theme.primaryDark
import com.homelandpay.themoviedb.ui.theme.primaryDarkVariant
import com.homelandpay.themoviedb.uti.Resource

import com.homelandpay.themoviedb.util.Constants.IMAGE_BASE_UR
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.log

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val popularMovies = homeViewModel.popularMovies.value.collectAsLazyPagingItems()

    val upcomingMovies = homeViewModel.upcomingMovies.value.collectAsLazyPagingItems()
    val savedPopularMovies = homeViewModel.getTestMovies.observeAsState()
    val popularCategoryMovies = homeViewModel.mGetPopular()
    val upcomingCategoryMovies = homeViewModel.mGetUpcoming()

    if (!popularCategoryMovies.isNullOrEmpty() && !upcomingCategoryMovies.isNullOrEmpty()) {

        Column {
            Text(text = "Popular", color = Color.White)
            LazyRow {
                items(popularCategoryMovies) {
                    MovieItemNew(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                navController.navigate(Routes.detailScreenWithArgs(it.id ?: 0))
                            },
                        imageUrl = "$IMAGE_BASE_UR/${it.posterPath}",
                        isLiked = homeViewModel.isAFavorite(it.id).observeAsState().value ?: false,
                        onClick = {
                            val movie = it.copy(
                                isFavourite =!it.isFavourite
                            )
                            homeViewModel.updateMovie(movie)


                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Upcoming", color = Color.White)
            LazyRow {
                items(upcomingCategoryMovies) {
                    MovieItemNew(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                navController.navigate(Routes.detailScreenWithArgs(it.id ?: 0))
                            },
                        imageUrl = "$IMAGE_BASE_UR/${it.posterPath}",
                        isLiked =  homeViewModel.isAFavorite(it.id).observeAsState().value ?: false,
                        onClick = {

                            //  homeViewModel.onFavouriteIconClicked(it.id, it.isFavourite)

                            val movie = it.copy(
                                isFavourite =true
                            )
                            homeViewModel.updateMovie(movie)


                            Log.d("@CatTest", it.toString())
                        }
                    )
                }
            }

        }


    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryDark)
        ) {
            Text(
                text = "Popular Movies",
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp),
                textAlign = TextAlign.Start,

                )
            Box(
                Modifier
                    .fillMaxWidth(),
            ) {

                LazyRow {
                    items(popularMovies) { movie ->
                        MovieItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable {
                                    navController.navigate(
                                        Routes.detailScreenWithArgs(
                                            movie?.id ?: 0
                                        )
                                    )
                                },
                            imageUrl = "$IMAGE_BASE_UR/${movie?.posterPath}"
                        )

                    }

                    if (popularMovies.loadState.append == LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }

                        }
                    }
                }
                popularMovies.apply {
                    loadState
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier,
                                color = Color.Gray,
                                strokeWidth = 2.dp
                            )
                        }

                        is LoadState.Error -> {
                            val e = popularMovies.loadState.refresh as LoadState.Error
                            Text(
                                text = when (e.error) {
                                    is HttpException -> {
                                        "Oops, something went wrong!"
                                    }

                                    is IOException -> {
                                        "Couldn't reach server, check your internet connection!"
                                    }

                                    else -> {
                                        "Unknown error occurred"
                                    }
                                },
                                textAlign = TextAlign.Center,
                                color = Color.Red
                            )
                        }

                        else -> {
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Upcoming Movies",
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp),
                textAlign = TextAlign.Start,

                )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(content = {
                    items(upcomingMovies) { film ->

                        MovieItem(
                            modifier = Modifier
                                .height(200.dp)
                                .width(130.dp)
                                .clickable {
                                    navController.navigate(
                                        Routes.detailScreenWithArgs(
                                            film?.id ?: 0
                                        )
                                    )
                                },
                            imageUrl = "$IMAGE_BASE_UR/${film?.posterPath}"
                        )
                    }
                    if (upcomingMovies.loadState.append == LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }


                        }
                    }
                }
                )

                upcomingMovies.apply {
                    loadState
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier,
                                color = Color.Gray,
                                strokeWidth = 2.dp
                            )
                        }

                        is LoadState.Error -> {
                            val e = upcomingMovies.loadState.refresh as LoadState.Error
                            Text(
                                text = when (e.error) {
                                    is HttpException -> {
                                        "Oops, something went wrong!"
                                    }

                                    is IOException -> {
                                        "Couldn't reach server, check your internet connection!"
                                    }

                                    else -> {
                                        "Unknown error occurred"
                                    }
                                },
                                textAlign = TextAlign.Center,
                                color = Color.Red
                            )
                        }

                        else -> {}
                    }
                }
            }
        }


    }
}