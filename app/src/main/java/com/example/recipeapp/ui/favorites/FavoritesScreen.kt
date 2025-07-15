package com.example.recipeapp.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar

@Composable
fun FavoritesScreen(

    navController: NavHostController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TopBar(navController)

        Text(
            text = "즐겨찾기",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
