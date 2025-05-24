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

    val favorites: StateFlow<List<FavoriteMovie>> =
        repository.getAllFavorites()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun isFavorite(movieId: Int): StateFlow<Boolean> {
        return repository.isFavorite(movieId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            if (isFavorite(movie.id).value) {
                removeFromFavorites(movie)
            } else {
                addToFavorites(movie)
            }
        }
    }

    fun addToFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorite = FavoriteMovie(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                rating = movie.rating
            )
            repository.addFavorite(favorite)
        }
    }

    fun removeFromFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorite = FavoriteMovie(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ?: "",
                rating = movie.rating
            )
            repository.removeFavorite(favorite)
        }
    }
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