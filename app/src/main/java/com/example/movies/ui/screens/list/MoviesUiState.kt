package com.example.movies.ui.screens.list

import com.example.movies.data.remote.MovieDto

sealed interface MoviesUiState {
    data class Success(val movies: List<MovieDto>) : MoviesUiState
    data object Loading : MoviesUiState
    data object Error : MoviesUiState
}