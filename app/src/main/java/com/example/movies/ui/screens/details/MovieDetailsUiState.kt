package com.example.movies.ui.screens.details

import com.example.movies.domain.CastMember
import com.example.movies.domain.Movie
import com.example.movies.domain.Video

data class MovieDetailsUiState(
    val movie: Movie,
    val trailer: Video?,
    val cast: List<CastMember>
)