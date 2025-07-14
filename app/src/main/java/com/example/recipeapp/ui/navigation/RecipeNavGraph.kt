package com.example.recipeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.ui.home.HomeScreen
import com.example.recipeapp.ui.ingredients.IngredientsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Ingredients : Screen("ingredients")
}

@Composable
fun RecipeNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToIngredients = {
                    navController.navigate(Screen.Ingredients.route)
                },
                navController = navController
            )
        }

        composable(Screen.Ingredients.route) {
            IngredientsScreen(
                onNext = { ingredientsList ->
                    // 추천 요리 로직
                    println("입력된 재료들: $ingredientsList")
                },
                navController = navController
            )
        }
    }
}
