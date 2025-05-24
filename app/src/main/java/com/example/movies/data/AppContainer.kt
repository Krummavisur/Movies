package com.example.movies.data

import android.app.Application
import androidx.room.Room
import com.example.movies.data.local.AppDatabase
import com.example.movies.data.local.FavoriteMovieDao
import com.example.movies.network.MoviesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val moviesRepository: MoviesRepository
    val appDatabase: AppDatabase
    val favoriteMovie: FavoriteMovieDao
}
class DefaultAppContainer(application: Application): AppContainer {

    private val baseUrl = "https://api.themoviedb.org/3/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: MoviesApiService by lazy {
            retrofit.create(MoviesApiService::class.java)
    }
    override val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "movies-database"
        ).build()
    }
    override val favoriteMovie: FavoriteMovieDao by lazy {
        appDatabase.favoriteMovieDao()
    }
    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService, favoriteMovie) //вероятно_нужно_будет_передать FavoriteMoviesDao
    }
}