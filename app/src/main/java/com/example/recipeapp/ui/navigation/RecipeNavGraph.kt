package com.example.recipeapp.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.ui.home.HomeScreen
import com.example.recipeapp.ui.ingredients.IngredientsScreen
import com.example.recipeapp.ui.favorites.FavoritesScreen
import com.example.recipeapp.ui.shopping.ShoppingScreen
import com.example.recipeapp.ui.fridge.FridgeScreen
import com.example.recipeapp.ui.fridge.FridgeViewModel

sealed class Screen(val route: String) {
    object Home        : Screen("home")
    object Ingredients : Screen("ingredients")
    object Favorites   : Screen("favorites")
    object Shopping    : Screen("shopping")       // ← Screen("shopping") 형태로 수정
    object Fridge    : Screen("fridge")
}
@Composable
fun RecipeNavGraph(
    navController: NavHostController,
    fridgeViewModel: FridgeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
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
                    println("입력된 재료들: $ingredientsList")
                },
                navController = navController,
                fridgeViewModel = fridgeViewModel // 전달해줌
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }

        composable(Screen.Shopping.route) {
            ShoppingScreen(navController = navController)
        }

        composable(Screen.Fridge.route) {
            FridgeScreen(
                navController = navController,
                viewModel = fridgeViewModel // 여기도 전달해줘야 함
            )
        }
    }
}
