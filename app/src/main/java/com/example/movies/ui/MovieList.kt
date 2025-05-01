package com.example.movies.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movies.model.Movie

@Composable
fun MovieListScreen(){
    LazyColumn(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
     item {
            MovieItem(
            )
        }
    }
}
@Composable
fun MovieItem(
    movie: Movie
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {}
    ) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = movie.title,
            modifier = Modifier
                .size(width = 100.dp, height = 150.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
        Column {
            Text(
                text = movie.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Рейтинг фильма: ${movie.rating}"
            )
        }
    }
}
