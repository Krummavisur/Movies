package com.example.movies.ui.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.repository.MoviesRepository
import com.example.movies.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState: StateFlow<MovieListUiState> = _uiState

    val moviesPagingFlow: Flow<PagingData<Movie>> =
        repository.getPopularMoviesPaged()
            .cachedIn(viewModelScope)

    init {
        fetchMovies()
    }
    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MovieListUiState.Loading
            try {
                val movies = repository.getPopularMovies()
                _uiState.value = MovieListUiState.Success(movies)
            } catch (e: Exception) {
                Log.e("MoviesListViewModel", "Error fetching movies", e)
                _uiState.value = MovieListUiState.Error
            }
        }
    }
}