package com.example.recipeapp.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar
import com.example.recipeapp.data.RecipeSamples
import com.example.recipeapp.data.FavoritesStore
import com.example.recipeapp.ui.ingredients.RecipeCard
import androidx.compose.ui.unit.dp
@Composable
fun FavoritesScreen(
    navController: NavHostController
) {
    val favoriteIds by FavoritesStore.ids.collectAsState()
    val favoriteRecipes = remember(favoriteIds) {
        RecipeSamples.sampleRecipes.filter { favoriteIds.contains(it.id) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(navController)

        LazyColumn {
            items(favoriteRecipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    isFavorite = true,
                    onToggleFavorite = { FavoritesStore.toggle(recipe.id) }
                )
            }
        }
    }
}
