package com.example.movies.ui.screens.favorites
import com.example.movies.domain.Movie

sealed interface FavoriteMovieUiState {
    data class Success(val movies: List<Movie>) : FavoriteMovieUiState
    data object Loading : FavoriteMovieUiState
    data object Error : FavoriteMovieUiState
}