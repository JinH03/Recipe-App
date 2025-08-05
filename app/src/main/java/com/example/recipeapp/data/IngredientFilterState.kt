package com.example.recipeapp.data
/*필터 상태 적용 하기위함*/
data class IngredientFilterState(
    val selectedIngredients: List<String>,
    val useOnlyFridge: Boolean,
    val selectedAllergies: Set<String>,
    val vegetarianOnly: Boolean
)
