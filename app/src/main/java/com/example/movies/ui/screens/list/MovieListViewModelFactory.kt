package com.example.movies.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.data.repository.MoviesRepository

class MovieListViewModelFactory(
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieListViewModel(repository) as T
    }
}