package com.example.movies.model

import com.google.gson.annotations.SerializedName


data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("overview") val overview: String
)

data class MovieResponse(
    val page: Int,
    val results: List<Movie>
)

data class VideosResponse(
    val results: List<Video>
)

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)

data class CreditsResponse(
    val cast: List<CastMember>
)

data class CastMember(
    val cast_id: Int,
    val character: String,
    val name: String,
    val profile_path: String?
)