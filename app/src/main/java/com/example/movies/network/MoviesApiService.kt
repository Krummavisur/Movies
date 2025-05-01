package com.example.movies.network

import com.example.movies.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApiService {
    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
        @Query("page") page: Int = 1
    ): MovieResponse //можно добавить call если не заработает
}
