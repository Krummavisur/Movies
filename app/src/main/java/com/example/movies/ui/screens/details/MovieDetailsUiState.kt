package com.example.movies.ui.screens.details

import com.example.movies.data.remote.CastMemberDto
import com.example.movies.data.remote.MovieDto
import com.example.movies.data.remote.VideoDto

data class MovieDetailsUiState(
    val movie: MovieDto,
    val trailer: VideoDto?,
    val cast: List<CastMemberDto>
)