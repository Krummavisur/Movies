package com.example.movies.data

import com.example.movies.network.MoviesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val moviesRepository: MoviesRepository
}
class DefaultAppContainer(): AppContainer {

    private val baseUrl = "https://api.themoviedb.org/3/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: MoviesApiService by lazy {
            retrofit.create(MoviesApiService::class.java)
    }
    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService)
    }

}