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

    private val _uiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>> = _uiState

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val movies = repository.getPopularMovies()
                _uiState.value = UiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Ошибка")
            }
        }
    }
}
sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val message: String): UiState<Nothing>()
}
class MoviesViewModelFactory(
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesListViewModel(repository) as T
    }
}