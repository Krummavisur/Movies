package com.example.movies.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movies.MoviesApplication
import com.example.movies.model.Video
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel
    ) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current.applicationContext as MoviesApplication
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(context.container.moviesRepository)
    )
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    if (uiState != null) {
        val movie = uiState!!.movie
        val trailer = uiState!!.trailer
        val cast = uiState!!.cast
        val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
        val isFavorite = favoriteIds.contains(movie.id)

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    HeartButton(
                        onFavoriteClick = { favoritesViewModel.toggleFavorite(movie) },
                        isFavorite = isFavorite
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ Рейтинг: ${movie.rating}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                TrailerButton(
                    trailer = trailer
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (cast.isNotEmpty()) {
                    Text(
                        text = "Актёры",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(cast) { actor ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(100.dp)
                            ) {
                                val profileUrl = actor.profilePath?.let {
                                    "https://image.tmdb.org/t/p/w200$it"
                                }

                                if (profileUrl != null) {
                                    AsyncImage(
                                        model = profileUrl,
                                        contentDescription = actor.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(Color.Gray)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = actor.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        MovieDetailsSkeleton()
    }
}
@Composable
fun TrailerButton(trailer: Video?) {
    val context = LocalContext.current

    trailer?.let {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://www.youtube.com/watch?v=${trailer.key}".toUri()
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Смотреть трейлер")
            }
        }
    }
}
@Composable
fun HeartButton(
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
){
    IconButton(
        onClick = onFavoriteClick,
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Избранное",
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}
@Composable
fun MovieDetailsSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(12.dp))
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.shimmer()
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(4.dp))
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.shimmer()
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.3f)
                .clip(RoundedCornerShape(4.dp))
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.shimmer()
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}