package com.example.movies.ui.screens.list

import com.example.movies.domain.Movie

sealed interface MoviesListUiState {
    data class Success(val movies: List<Movie>) : MoviesListUiState
    data object Loading : MoviesListUiState
    data object Error : MoviesListUiState
}