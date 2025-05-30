package com.example.movies.data.mapper

import com.example.movies.data.local.FavoriteMovieEntity
import com.example.movies.data.remote.MovieDto
import com.example.movies.domain.Movie

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    rating = rating,
    overview = overview
)
fun Movie.toEntity(): FavoriteMovieEntity = FavoriteMovieEntity(
    id = id,
    title = title,
    posterPath = posterPath ?: "",
    rating = rating,
)
fun FavoriteMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    rating = rating,
    overview = ""
)