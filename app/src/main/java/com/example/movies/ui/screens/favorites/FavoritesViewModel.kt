package com.example.movies.ui.screens.favorites
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MoviesRepository
import com.example.movies.data.local.FavoriteMovieEntity
import com.example.movies.domain.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
private val repository: MoviesRepository
) : ViewModel() {

    private val _favorites: StateFlow<List<Movie>> =
        repository.getAllFavorites()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<Movie>> = _favorites

    private val _uiState = MutableStateFlow<FavoriteMoviesUiState>(FavoriteMoviesUiState.Loading)
    val uiState: StateFlow<FavoriteMoviesUiState> = _uiState

    val favoriteIds: StateFlow<Set<Int>> = _favorites
        .map { it.map { movie -> movie.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        viewModelScope.launch {
            favorites.collect { list ->
                _uiState.value = FavoriteMoviesUiState.Success(list)
            }
        }
    }
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
            FavoriteMovieEntity(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                rating = movie.rating
            )
        )
    }

    private suspend fun removeFromFavorites(movie: Movie) {
        repository.removeFavorite(
            FavoriteMovieEntity(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                rating = movie.rating
            )
        )
    }
}