package com.example.movies

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movies.ui.screens.MovieDetailsScreen
import com.example.movies.ui.screens.MovieDetailsViewModel
import com.example.movies.ui.screens.MovieDetailsViewModelFactory
import com.example.movies.ui.screens.MovieListScreen
import com.example.movies.ui.screens.MoviesListViewModel
import com.example.movies.ui.screens.MoviesViewModelFactory

enum class MoviesScreen(val route: String) {
    List("movies"),
    Details("details/{movieId}");

    companion object {
        fun fromRoute(route: String?): MoviesScreen =
            values().firstOrNull { route?.startsWith(it.route) == true } ?: List
    }
}

@ExperimentalMaterial3Api
@Composable
fun MoviesAppBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesApp(
    navController: NavHostController = rememberNavController(),
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MoviesScreen.fromRoute(currentBackStackEntry?.destination?.route)
    val context = LocalContext.current.applicationContext as MoviesApplication
    val moviesListViewModel: MoviesListViewModel = viewModel (
        factory = MoviesViewModelFactory(context.container.moviesRepository)
    )
    LaunchedEffect(Unit) {
        moviesListViewModel.fetchMovies()
    }
    Scaffold(
        topBar = {
            if (currentScreen == MoviesScreen.List) {
                MoviesAppBar(title = "Популярные фильмы")
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = MoviesScreen.List.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(MoviesScreen.List.route) {
                MovieListScreen(
                    onMovieClick = { movieId ->
                        Log.d("Navigation", "Navigating to details with movieId: $movieId")
                        navController.navigate("details/${movieId}")
                    }
                )
            }
            composable(
                route = MoviesScreen.Details.route,
                arguments = listOf(navArgument("movieId") { type = NavType.IntType }) //либо стринг либо инт
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                Log.d("DetailsScreen", "Received movieId: $movieId")
                val detailsViewModel: MovieDetailsViewModel = viewModel(
                    factory = MovieDetailsViewModelFactory(
                        movieId = movieId,
                        moviesViewModel = moviesListViewModel
                    )
                )
                MovieDetailsScreen(viewModel = detailsViewModel)
            }
        }
    }
}