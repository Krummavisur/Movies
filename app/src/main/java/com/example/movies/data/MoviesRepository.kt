package com.example.movies.data

import com.example.movies.model.CastMember
import com.example.movies.model.Movie
import com.example.movies.model.Video
import com.example.movies.network.MoviesApiService

interface MoviesRepository {
     suspend fun getPopularMovies(): List<Movie>
    suspend fun getVideos(movieId: Int): List<Video>
    suspend fun getCredits(movieId: Int): List<CastMember>
    suspend fun getMovieDetails(movieId: Int): Movie
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
    override suspend fun getVideos(movieId: Int): List<Video> {
        return apiService.getVideos(
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            movieId = movieId,
            language = "ru-RU",
        ).results
    }

    override suspend fun getCredits(movieId: Int): List<CastMember> {
        return apiService.getCredits(
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            movieId = movieId,
        ).cast
    }
    override suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.getMovieDetails(
            movieId = movieId,
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            language = "ru-RU"
        )
    }
}