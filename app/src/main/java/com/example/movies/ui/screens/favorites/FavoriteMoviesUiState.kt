package com.example.movies.ui.screens.favorites
import com.example.movies.data.local.FavoriteMovieEntity

sealed interface FavoriteMoviesUiState {
    data class Success(val movies: List<FavoriteMovieEntity>) : FavoriteMoviesUiState
    data object Loading : FavoriteMoviesUiState
    data object Error : FavoriteMoviesUiState
}