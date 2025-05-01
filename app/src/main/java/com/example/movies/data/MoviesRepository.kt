package com.example.movies.data

import com.example.movies.model.Movie
import com.example.movies.network.MoviesApiService

interface MoviesRepository {
     suspend fun getPopularMovies(): List<Movie>
}
class NetworkMoviesRepository(
    private val apiService: MoviesApiService
): MoviesRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        return apiService.getMovies(
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            language = "ru-RU",
            page = 1
        ).results
    }
}