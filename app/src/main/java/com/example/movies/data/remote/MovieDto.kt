package com.example.movies.data.remote

import com.google.gson.annotations.SerializedName

data class MovieDto(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("overview") val overview: String
)
