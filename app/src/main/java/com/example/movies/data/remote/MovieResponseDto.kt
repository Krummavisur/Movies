package com.example.movies.data.remote

data class MovieResponseDto(
    val page: Int,
    val results: List<MovieDto>
)