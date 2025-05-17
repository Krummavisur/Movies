package com.example.movies.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movies.MoviesApplication
import com.example.movies.model.Movie

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current.applicationContext as MoviesApplication
    val viewModel: MoviesListViewModel = viewModel(
        factory = MoviesViewModelFactory(context.container.moviesRepository)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMovies()
    }

    when (uiState) {
        is MoviesUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()
                .padding(contentPadding),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is MoviesUiState.Success -> {
            val movies = (uiState as MoviesUiState.Success).movies
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .clipToBounds(),
                contentPadding = contentPadding,
            ) {
                items(movies, key = { it.id }) { movie ->
                    MovieItem(
                        movie = movie,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }
        }

        is MoviesUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize()
                .padding(contentPadding),
                contentAlignment = Alignment.Center) {
                Text("Произошла ошибка", color = Color.Red, fontSize = 25.sp)
            }
        }
    }
}
@Composable
fun MovieItem(
    modifier: Modifier,
    movie: Movie,
    onClick: () -> Unit
) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⭐ ${movie.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
