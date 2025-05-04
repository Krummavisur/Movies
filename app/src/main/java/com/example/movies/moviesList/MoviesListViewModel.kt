package com.example.movies.moviesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MoviesRepository
import com.example.movies.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MoviesUiState.Loading
            try {
                val movies = repository.getPopularMovies()
                _uiState.value = MoviesUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = MoviesUiState.Error
            }
        }
    }
}
sealed interface MoviesUiState {
    data class Success(val movies: List<Movie>) : MoviesUiState
    data object Loading : MoviesUiState
    data object Error : MoviesUiState
}
class MoviesViewModelFactory(
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesListViewModel(repository) as T
    }
}