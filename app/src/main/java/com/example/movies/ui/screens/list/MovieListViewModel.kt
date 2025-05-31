package com.example.movies.ui.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesListUiState>(MoviesListUiState.Loading)
    val uiState: StateFlow<MoviesListUiState> = _uiState

    init {
        fetchMovies()
    }
    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MoviesListUiState.Loading
            try {
                val movies = repository.getPopularMovies()
                _uiState.value = MoviesListUiState.Success(movies)
            } catch (e: Exception) {
                Log.e("MoviesListViewModel", "Error fetching movies", e)
                _uiState.value = MoviesListUiState.Error
            }
        }
    }
}