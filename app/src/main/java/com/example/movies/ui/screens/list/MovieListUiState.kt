package com.example.movies.ui.screens.list

import com.example.movies.domain.Movie

sealed interface MovieListUiState {
    data class Success(val movies: List<Movie>) : MovieListUiState
    data object Loading : MovieListUiState
    data object Error : MovieListUiState
}