package com.example.movies.domain

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val rating: Float,
    val overview: String
)