package com.example.recipeapp.ui.shopping

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar

@Composable
fun ShoppingScreen(
    navController: NavHostController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TopBar(navController)

        Text(
            text = "장바구니",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
//