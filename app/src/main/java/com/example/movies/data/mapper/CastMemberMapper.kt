package com.example.movies.data.mapper

import com.example.movies.data.remote.CastMemberDto
import com.example.movies.domain.CastMember

fun CastMemberDto.toDomain(): CastMember = CastMember(
    castId = castId,
    character = character,
    name = name,
    profilePath = profilePath
)