package com.example.movies.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.data.repository.MoviesRepository

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
