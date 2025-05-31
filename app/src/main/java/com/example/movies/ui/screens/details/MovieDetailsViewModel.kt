package com.example.movies.ui.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MoviesRepository
import com.example.movies.data.mapper.toDomain
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
                    movie = movie.toDomain(),
                    trailer = trailer?.toDomain(),
                    cast = cast.map { it.toDomain() }
                )
            } catch (e: Exception){
                Log.e("MovieDetailsViewModel", "Ошибка при загрузке деталей", e)
            }
        }
    }
}
