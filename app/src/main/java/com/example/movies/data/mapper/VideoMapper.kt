package com.example.movies.data.mapper

import com.example.movies.data.remote.VideoDto
import com.example.movies.domain.Video

fun VideoDto.toDomain(): Video = Video(
    id = id,
    key = key,
    name = name,
    site = site,
    type = type
)