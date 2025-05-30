package com.example.movies.network

import com.example.movies.data.remote.CreditsResponseDto
import com.example.movies.data.remote.MovieDto
import com.example.movies.data.remote.MovieResponseDto
import com.example.movies.data.remote.VideosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApiService {
    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
        @Query("page") page: Int = 1
    ): MovieResponseDto

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
    ): VideosResponseDto

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): CreditsResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU"
    ): MovieDto
}
