package com.example.movies.model

import com.google.gson.annotations.SerializedName


data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Double
)

data class MovieResponse(
    val page: Int,
    val results: List<Movie>
)