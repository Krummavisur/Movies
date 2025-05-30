package com.example.movies.domain

data class CastMember(
    val castId: Int,
    val character: String,
    val name: String,
    val profilePath: String?
)