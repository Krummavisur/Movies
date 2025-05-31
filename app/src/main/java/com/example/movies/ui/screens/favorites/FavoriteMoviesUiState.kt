package com.example.movies.ui.screens.favorites
import com.example.movies.domain.Movie

sealed interface FavoriteMoviesUiState {
    data class Success(val movies: List<Movie>) : FavoriteMoviesUiState
    data object Loading : FavoriteMoviesUiState
    data object Error : FavoriteMoviesUiState
}