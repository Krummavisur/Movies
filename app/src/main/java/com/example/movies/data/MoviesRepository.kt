package com.example.movies.data

import com.example.movies.data.local.FavoriteMovieEntity
import com.example.movies.data.local.FavoriteMovieDao
import com.example.movies.data.mapper.toDomain
import com.example.movies.data.remote.CastMemberDto
import com.example.movies.data.remote.MovieDto
import com.example.movies.data.remote.VideoDto
import com.example.movies.domain.Movie
import com.example.movies.network.MoviesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MoviesRepository {
    //Retrofit
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getVideos(movieId: Int): List<VideoDto>
    suspend fun getCredits(movieId: Int): List<CastMemberDto>
    suspend fun getMovieDetails(movieId: Int): MovieDto
    //Room
    suspend fun addFavorite(movie: FavoriteMovieEntity)
    suspend fun removeFavorite(movie: FavoriteMovieEntity)
    fun getAllFavorites(): Flow<List<Movie>>
    fun isFavorite(id: Int): Flow<Boolean>
}
class NetworkMoviesRepository(
    private val apiService: MoviesApiService,
    private val favoriteDao: FavoriteMovieDao
): MoviesRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        return apiService.getMovies(
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            language = "ru-RU",
            page = 1
        ).results.map { it.toDomain() }
    }
    override suspend fun getVideos(movieId: Int): List<VideoDto> {
        return apiService.getVideos(
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            movieId = movieId,
            language = "ru-RU",
        ).results
    }

    override suspend fun getCredits(movieId: Int): List<CastMemberDto> {
        return apiService.getCredits(
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            movieId = movieId,
        ).cast
    }
    override suspend fun getMovieDetails(movieId: Int): MovieDto {
        return apiService.getMovieDetails(
            movieId = movieId,
            apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4",
            language = "ru-RU"
        )
    }
    override suspend fun addFavorite(movie: FavoriteMovieEntity) {
        favoriteDao.insert(movie)
    }

    override suspend fun removeFavorite(movie: FavoriteMovieEntity) {
        favoriteDao.delete(movie)
    }

    override fun getAllFavorites(): Flow<List<Movie>> {
        return favoriteDao.getAllFavorites().map { list -> list.map { it.toDomain() } }
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteDao.isFavorite(id)
    }
}