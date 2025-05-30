package com.example.movies.data.remote

import com.google.gson.annotations.SerializedName

data class CastMemberDto(
    @SerializedName("cast_id") val castId: Int,
    val character: String,
    val name: String,
    @SerializedName("profile_path") val profilePath: String?
)