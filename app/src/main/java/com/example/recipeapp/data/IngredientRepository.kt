package com.example.recipeapp.data

import kotlinx.coroutines.flow.Flow

class IngredientRepository(private val dao: IngredientDao) {

    val ingredients: Flow<List<IngredientEntity>> = dao.getAllIngredients()

    suspend fun addIngredient(ingredient: IngredientEntity) {
        dao.insert(ingredient)
    }

    suspend fun deleteIngredient(ingredient: IngredientEntity) {
        dao.delete(ingredient)
    }

    suspend fun updateIngredient(ingredient: IngredientEntity) {
        dao.update(ingredient)
    }
}
