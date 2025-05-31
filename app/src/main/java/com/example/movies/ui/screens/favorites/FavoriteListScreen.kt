package com.example.movies.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies.MoviesApplication
import com.example.movies.ui.screens.list.MovieItem
import com.example.movies.ui.screens.list.MovieItemSkeleton

@Composable
fun FavoritesListScreen(
    onMovieClick: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current.applicationContext as MoviesApplication
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(context.container.moviesRepository)
    )
    val favoriteMovies by favoritesViewModel.favorites.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()
    val uiState by favoritesViewModel.uiState.collectAsState()

    when (uiState) {
        is FavoriteMovieUiState.Loading -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(6) {
                    MovieItemSkeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                    )
                }
            }
        }
        is FavoriteMovieUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteMovies, key = { it.id }) { favoriteMovie ->
                    val isFavorite = favoriteIds.contains(favoriteMovie.id)
                    MovieItem(
                        movie = favoriteMovie,
                        onClick = { onMovieClick(favoriteMovie.id) },
                        onFavoriteClick = { favoritesViewModel.toggleFavorite(favoriteMovie) },
                        isFavorite = isFavorite
                    )
                }
            }
        }
        is FavoriteMovieUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Произошла ошибка", color = Color.Red, fontSize = 25.sp)
            }
        }
    }
}