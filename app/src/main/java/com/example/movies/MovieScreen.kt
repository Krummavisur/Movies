package com.example.movies

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.movies.ui.screens.ListFavoritesScreen
import com.example.movies.ui.screens.MovieDetailsScreen
import com.example.movies.ui.screens.MovieDetailsViewModel
import com.example.movies.ui.screens.MovieDetailsViewModelFactory
import com.example.movies.ui.screens.MovieListScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

enum class MoviesScreen(val route: String) {
    List("movies"),
    Details("details/{movieId}"),
    Favorites("favorites");

    companion object {
        fun fromRoute(route: String?): MoviesScreen =
            values().firstOrNull { route?.startsWith(it.route) == true } ?: List
    }
}

@ExperimentalMaterial3Api
@Composable
fun MoviesAppBar(
    title: String,
    showFavoritesButton: Boolean,
    onFavoritesClick: () -> Unit,
    showBackButton: Boolean,
    onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }
        },
        actions = {
            if (showFavoritesButton) {
                IconButton(onClick = onFavoritesClick) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Избранное",
                        tint = Color.Yellow
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MoviesApp(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MoviesScreen.fromRoute(currentBackStackEntry?.destination?.route)
    val context = LocalContext.current.applicationContext as MoviesApplication
    Scaffold(
        topBar = {
            when (currentScreen) {
                MoviesScreen.List -> {
                    MoviesAppBar(
                        title = "Популярные фильмы",
                        showFavoritesButton = true,
                        onFavoritesClick = { navController.navigate(MoviesScreen.Favorites.route) },
                        showBackButton = false,
                        onBackClick = {}
                    )
                }
                MoviesScreen.Favorites -> {
                    MoviesAppBar(
                        title = "Избранное",
                        showFavoritesButton = false,
                        showBackButton = true,
                        onBackClick = { navController.popBackStack() },
                        onFavoritesClick = {}
                    )
                }
                else -> {}
            }
        }
    ) { padding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = MoviesScreen.List.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 4 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 4 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
            }
        ) {
            composable(MoviesScreen.List.route) {
                MovieListScreen(
                    onMovieClick = { movieId ->
                        Log.d("Navigation", "Navigating to details with movieId: $movieId")
                        navController.navigate("details/${movieId}")
                    },
                    contentPadding = padding
                )
            }
            composable(
                route = MoviesScreen.Details.route,
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                Log.d("DetailsScreen", "Received movieId: $movieId")
                val detailsViewModel: MovieDetailsViewModel = viewModel(
                    factory = MovieDetailsViewModelFactory(
                        movieId = movieId,
                        repository = context.container.moviesRepository
                    )
                )

                MovieDetailsScreen(viewModel = detailsViewModel)
            }
            composable(MoviesScreen.Favorites.route) {
                ListFavoritesScreen(
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    }
                )
            }
        }
    }
}