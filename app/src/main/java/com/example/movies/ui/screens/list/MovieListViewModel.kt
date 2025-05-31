package com.example.movies.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.data.repository.MoviesRepository
import com.example.movies.domain.Movie
import kotlinx.coroutines.flow.Flow

class MovieListViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    val moviesPagingFlow: Flow<PagingData<Movie>> =
        repository.getPopularMoviesPaged()
            .cachedIn(viewModelScope)
}