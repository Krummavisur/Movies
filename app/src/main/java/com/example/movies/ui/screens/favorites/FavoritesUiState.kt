package com.example.movies.ui.screens.favorites
import com.example.movies.domain.Movie

sealed interface FavoritesUiState {
    data class Success(val movies: List<Movie>) : FavoritesUiState
    data object Loading : FavoritesUiState
    data object Error : FavoritesUiState
}