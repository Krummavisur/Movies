package com.example.movies.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val moviesListViewModel: MoviesListViewModel,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<Movie?>(null)
    val uiState: StateFlow<Movie?> = _uiState

    init {
        viewModelScope.launch {
            moviesListViewModel.uiState.collect { state ->
                Log.d("MovieDetailsViewModel", "MoviesListViewModel uiState changed: $state")
                if (state is MoviesUiState.Success) {
                    val movie = state.movies.find { it.id == movieId }
                    Log.d("MovieDetailsViewModel", "Found movie: $movie")
                    _uiState.value = movie
                }
            }
        }
    }
}
class MovieDetailsViewModelFactory(
    private val moviesViewModel: MoviesListViewModel,
    private val movieId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(moviesViewModel, movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}