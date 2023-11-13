package com.homelandpay.themoviedb.appComponent

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.LocalWindowInsets
import com.homelandpay.themoviedb.R
import com.homelandpay.themoviedb.appNav.Routes
import com.homelandpay.themoviedb.data.local.Favorite
import com.homelandpay.themoviedb.ui.screen.FavoritesViewModel
import com.homelandpay.themoviedb.ui.theme.primaryPink
import com.homelandpay.themoviedb.uti.Resource
import kotlin.math.max
import kotlin.math.min

val AppBarCollapsedHeight = 50.dp
val AppBarExpendedHeight = 420.dp

@Composable
fun MovieItem(modifier: Modifier, imageUrl: String) {
    Card(modifier = modifier.padding(4.dp)) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    placeholder(R.drawable.placeholder_image)
                    crossfade(true)
                }
            ),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = "Movie Banner"
        )
    }
}



@Composable
fun MovieItemNew(modifier: Modifier, imageUrl: String,
                 isLiked: Boolean,
                 onClick: () -> Unit
) {
    //onClick: (isFav: Boolean) -> Unit,
    Card(modifier = modifier.padding(4.dp)) {
        Box (modifier = Modifier.fillMaxWidth()){
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        placeholder(R.drawable.placeholder_image)
                        crossfade(true)
                    }
                ),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = "Movie Banner"
            )

            IconButton(

                onClick = {
                    onClick()
                },
                modifier = Modifier.align(Alignment.TopEnd)

            ) {

                Icon(
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp),
                    imageVector = Icons.Filled.Favorite,
                    tint = if (isLiked) {
                        Color.Cyan
                    } else {
                        Color.LightGray
                    },
                    contentDescription = if (isLiked) {
                        ""
                    } else {
                        ""
                    }
                )
            }
        }

    }
}




@Composable
fun FilmInfo(
    scrollState: LazyListState,
    releaseDate: String,
    overview: String,

    ) {

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = "Release date",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = releaseDate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExpandableText(text = overview)
            }
        }

    }
}


@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 3,
) {
    var cutText by remember(text) { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val seeMoreSizeState = remember { mutableStateOf<IntSize?>(null) }
    val seeMoreOffsetState = remember { mutableStateOf<Offset?>(null) }

    // getting raw values for smart cast
    val textLayoutResult = textLayoutResultState.value
    val seeMoreSize = seeMoreSizeState.value
    val seeMoreOffset = seeMoreOffsetState.value

    LaunchedEffect(text, expanded, textLayoutResult, seeMoreSize) {
        val lastLineIndex = minimizedMaxLines - 1
        if (!expanded && textLayoutResult != null && seeMoreSize != null
            && lastLineIndex + 1 == textLayoutResult.lineCount
            && textLayoutResult.isLineEllipsized(lastLineIndex)
        ) {
            var lastCharIndex = textLayoutResult.getLineEnd(lastLineIndex, visibleEnd = true) + 1
            var charRect: Rect
            do {
                lastCharIndex -= 1
                charRect = textLayoutResult.getCursorRect(lastCharIndex)
            } while (
                charRect.left > textLayoutResult.size.width - seeMoreSize.width
            )
            seeMoreOffsetState.value = Offset(charRect.left, charRect.bottom - seeMoreSize.height)
            cutText = text.substring(startIndex = 0, endIndex = lastCharIndex)
        }
    }

    Box(modifier) {
        Text(
            color = Color.LightGray,
            text = cutText ?: text,
            fontSize = 13.sp,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = false
                },
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResultState.value = it },
        )
        if (!expanded) {
            val density = LocalDensity.current
            Text(
                // Fixme: Use your app theme color
                color = Color.Gray,
                text = "... See more",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                onTextLayout = { seeMoreSizeState.value = it.size },
                modifier = Modifier
                    .then(
                        if (seeMoreOffset != null)
                            Modifier.offset(
                                x = with(density) { seeMoreOffset.x.toDp() },
                                y = with(density) { seeMoreOffset.y.toDp() },
                            )
                        else Modifier
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        expanded = true
                        cutText = null
                    }
                    .alpha(if (seeMoreOffset != null) 1f else 0f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmImageBanner(
    scrollState: LazyListState,
    posterUrl: String,
    filmName: String,
    filmId: Int,
    filmType: String,
    releaseDate: String,
    rating: Float,
    navController: NavController,
    viewModel: FavoritesViewModel
) {

    val context = LocalContext.current

    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight

    val maxOffset = with(LocalDensity.current) {
        imageHeight.roundToPx()
    } - LocalWindowInsets.current.systemBars.layoutInsets.top

    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)

    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset


    Column {

        Box (modifier = Modifier.fillMaxWidth()){
            Image(
                painter = rememberImagePainter(
                    data = posterUrl,
                    builder = {
                        placeholder(R.drawable.placeholder_image)
                        crossfade(true)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offsetProgress
                    },
                contentScale = ContentScale.Crop,
                contentDescription = "Movie Banner"
            )

            FilmNameAndRating(
                modifier = Modifier.align(Alignment.BottomCenter),
                filmName = filmName,
                rating = rating
            )
        }
       Box (modifier = Modifier
           .fillMaxSize()
           .background(Color.Transparent,),
           contentAlignment = Alignment.BottomCenter
       ){
           Button(onClick = {
               navController.navigate(Routes.Favourite_SCREEN)
           }) {
               Text(text = "Favourite Screen")
           }
       }





    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 10.dp)
    ) {
        CircularBackButtons(
            onClick = {
                navController.popBackStack()
            }
        )


        CircularFavoriteButtons(
            isLiked = viewModel.isAFavorite(filmId).observeAsState().value != null,
            onClick = { isFav ->
                if (isFav) {
                    Toast.makeText(context, "Already added to your favorites", Toast.LENGTH_SHORT)
                        .show()
                    return@CircularFavoriteButtons
                } else {
                    viewModel.insertFavorite(
                        Favorite(
                            favorite = true,
                            mediaId = filmId,
                            mediaType = filmType,
                            image = posterUrl,
                            title = filmName,
                            releaseDate = releaseDate,
                            rating = rating
                        )
                    )
                }
            }
        )
    }
}


@Composable
fun FilmNameAndRating(
    modifier: Modifier = Modifier,
    filmName: String,
    rating: Float
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth(0.83f),
                text = filmName,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            VoteAverageRatingIndicator(
                modifier = modifier
                    .fillMaxWidth(0.17f),
                percentage = rating
            )
        }
    }
}

@Composable
fun VoteAverageRatingIndicator(
    modifier: Modifier = Modifier,
    percentage: Float,
    number: Int = 10,
    fontSize: TextUnit = 16.sp,
    radius: Dp = 20.dp,
    color: Color = primaryPink,
    strokeWidth: Dp = 3.dp,
    animationDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animDelay
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(
            modifier = Modifier
                .size(radius * 2f)
        ) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = (360 * (currentPercentage.value * 0.1)).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${(currentPercentage.value * number).toInt()}%",
            color = primaryPink,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CircularBackButtons(
    color: Color = Color.Gray,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        contentPadding = PaddingValues(),
        shape = CircleShape,
        elevation = elevation,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.3f),
            contentColor = color
        )
    ) {
        IconButton(onClick = {
            onClick()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow_24),
                tint = Color.Gray,
                contentDescription = null
            )
        }
    }
}


@Composable
fun CircularFavoriteButtons(
    isLiked: Boolean,
    onClick: (isFav: Boolean) -> Unit
) {
    IconButton(
        onClick = {
            onClick(isLiked)
        }) {

        Icon(
            modifier = Modifier
                .width(38.dp)
                .height(38.dp),
            imageVector = Icons.Filled.Favorite,
            tint = if (isLiked) {
                Color.Cyan
            } else {
                Color.LightGray
            },
            contentDescription = if (isLiked) {
                ""
            } else {
                ""
            }
        )
    }
}