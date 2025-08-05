package com.example.recipeapp.data

data class IngredientFilterState(
    val selectedIngredients: List<String>,
    val useOnlyFridge: Boolean,
    val selectedAllergies: Set<String>,
    val vegetarianOnly: Boolean
)
