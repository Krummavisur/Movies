package com.example.movies.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieDetailsViewModel(
    private val moviesListViewModel: MoviesListViewModel,
    private val movieIndex: Int
): ViewModel() {

    private val _uiState = MutableStateFlow<Movie?>(null)
    val uiState: StateFlow<Movie?> = _uiState

    private fun getMovieDetails(){
        val movies = (moviesListViewModel.uiState.value as? MoviesUiState.Success)?.movies
        val selectedMovie = movies?.getOrNull(movieIndex)
        _uiState.value = selectedMovie
    }
}
class MovieDetailsViewModelFactory(
    private val moviesViewModel: MoviesListViewModel,
    private val movieIndex: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(moviesViewModel, movieIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}