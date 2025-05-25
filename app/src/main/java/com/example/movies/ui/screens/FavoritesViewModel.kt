package com.example.movies.ui.screens
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MoviesRepository
import com.example.movies.data.local.FavoriteMovie
import com.example.movies.model.Movie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _favorites: StateFlow<List<FavoriteMovie>> =
        repository.getAllFavorites()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteMovie>> = _favorites

    private val _uiState = MutableStateFlow<FavoriteMoviesUiState>(FavoriteMoviesUiState.Loading)
    val uiState: StateFlow<FavoriteMoviesUiState> = _uiState

    val favoriteIds: StateFlow<Set<Int>> = _favorites
        .map { it.map { movie -> movie.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            if (favoriteIds.value.contains(movie.id)) {
                removeFromFavorites(movie)
            } else {
                addToFavorites(movie)
            }
        }
    }

    private suspend fun addToFavorites(movie: Movie) {
        repository.addFavorite(
            FavoriteMovie(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                rating = movie.rating
            )
        )
    }

    private suspend fun removeFromFavorites(movie: Movie) {
        repository.removeFavorite(
            FavoriteMovie(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                rating = movie.rating
            )
        )
    }
}
sealed interface FavoriteMoviesUiState {
    data class Success(val movies: List<FavoriteMovie>) : FavoriteMoviesUiState
    data object Loading : FavoriteMoviesUiState
    data object Error : FavoriteMoviesUiState
}
class FavoritesViewModelFactory(
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}