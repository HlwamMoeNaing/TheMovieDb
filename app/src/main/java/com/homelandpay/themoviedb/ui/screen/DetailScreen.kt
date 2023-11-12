package com.homelandpay.themoviedb.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.homelandpay.themoviedb.appComponent.FilmImageBanner
import com.homelandpay.themoviedb.appComponent.FilmInfo
import com.homelandpay.themoviedb.appNav.Routes
import com.homelandpay.themoviedb.data.model.MovieDetails
import com.homelandpay.themoviedb.ui.theme.primaryDark
import com.homelandpay.themoviedb.uti.Resource
import com.homelandpay.themoviedb.util.Constants.IMAGE_BASE_UR

@Composable
fun DetailScreen(
    navController: NavController,
    itemId: Int,
    detailViewModel: DetailViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel()

) {
    var itemId by remember {
        mutableStateOf(itemId)
    }

    val scrollState = rememberLazyListState()

    val details = produceState<Resource<MovieDetails>>(initialValue = Resource.Loading()) {
        value = detailViewModel.getMovieDetails(itemId)
    }.value

    Box(modifier = Modifier.background(primaryDark)) {
        if (details is Resource.Success) {
            FilmInfo(
                scrollState = scrollState,
                overview = details.data?.overview.toString(),
                releaseDate = details.data?.releaseDate.toString(),
            )
            FilmImageBanner(
                scrollState = scrollState,
                posterUrl = "${IMAGE_BASE_UR}/${details.data?.posterPath}",
                filmName = details.data?.title.toString(),
                filmId = details.data?.id!!,
                filmType = "movie",
                releaseDate = details.data.releaseDate.toString(),
                rating = details.data.voteAverage?.toFloat()!!,
                navController = navController,
                viewModel = favoritesViewModel
            )
        } else {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }


        }
    }

}