package com.example.movies.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movies.MoviesApplication
import com.example.movies.model.Movie

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is MoviesUiState.Success -> {
            val movies = (uiState as MoviesUiState.Success).movies
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onClick = {onMovieClick(movie.id)})
                }
            }
        }

        is MoviesUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Произошла ошибка", color = Color.Red)
            }
        }
    }
}
@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {onClick()}
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .size(width = 100.dp, height = 150.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Рейтинг фильма: ${movie.rating}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
