package com.example.movies.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MoviesRepository
import com.example.movies.model.CastMember
import com.example.movies.model.Movie
import com.example.movies.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MoviesRepository,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState?>(null)
    val uiState: StateFlow<MovieDetailsUiState?> = _uiState

    init {
        loadMovieDetails()
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(movieId)
                val videos = repository.getVideos(movieId)
                val cast = repository.getCredits(movieId)
                val trailer = videos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }

                _uiState.value = MovieDetailsUiState(
                    movie = movie,
                    trailer = trailer,
                    cast = cast
                )
            } catch (e: Exception){
                Log.e("MovieDetailsViewModel", "Ошибка при загрузке деталей", e)
            }
        }
    }
}
class MovieDetailsViewModelFactory(
    private val repository: MoviesRepository,
    private val movieId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(repository, movieId) as T
        }
        throw IllegalArgumentException("Error")
    }
}
data class MovieDetailsUiState(
    val movie: Movie,
    val trailer: Video?,
    val cast: List<CastMember>
)