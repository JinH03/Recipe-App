package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.IngredientRepository
import com.example.recipeapp.ui.fridge.FridgeViewModel
import com.example.recipeapp.ui.fridge.FridgeViewModelFactory
import com.example.recipeapp.ui.navigation.RecipeNavGraph
import com.example.recipeapp.ui.theme.MyApplicationTheme
import com.example.recipeapp.data.FavoritesStore

class MainActivity : ComponentActivity() {

    private val fridgeViewModel: FridgeViewModel by viewModels {
        // ✅ Room + Repository + Factory 연결
        val dao = AppDatabase.getInstance(applicationContext).ingredientDao()
        val repository = IngredientRepository(dao)
        FridgeViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavoritesStore.init(applicationContext)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                RecipeNavGraph(
                    navController = navController,
                    fridgeViewModel = fridgeViewModel
                )
            }
        }
    }
}
