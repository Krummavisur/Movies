package com.example.movies.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.data.mapper.toDomain
import com.example.movies.domain.Movie
import com.example.movies.network.MoviesApiService

class MoviesPagingSource(
    private val api: MoviesApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = api.getMovies(apiKey = "b2b14ca051a4ae73dc8fb4b0756263f4", page = page)
            val movies = response.results.map { it.toDomain() }

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}