package com.example.recipeapp.ui.fridge

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.IngredientRepository
import com.example.recipeapp.model.Ingredient
import com.example.recipeapp.model.toEntity
import com.example.recipeapp.model.toUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FridgeViewModel(
    private val repository: IngredientRepository
) : ViewModel() {

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    init {
        loadIngredients()
    }

    private fun loadIngredients() {
        viewModelScope.launch {
            repository.ingredients
                .map { list ->
                    println("Repository returned: $list")  // 로그 확인용
                    list.map { it.toUiModel() }
                }
                .collect { uiList ->
                    _ingredients.value = uiList
                    println("Loaded ingredients: $uiList")  // 로그 확인용
                }
        }
    }


    fun addIngredient(name: String, count: Int, expiry: String, memo: String) {
        viewModelScope.launch {
            val ingredient = Ingredient(
                name = name,
                count = mutableIntStateOf(count),
                expiry = expiry,
                memo = memo
            )
            println("Adding ingredient to DB: $ingredient")  // 로그 추가
            repository.addIngredient(ingredient.toEntity())
        }
    }



    fun removeIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.deleteIngredient(ingredient.toEntity())
        }
    }

    fun increaseCount(ingredient: Ingredient) {
        val newCount = ingredient.count.value + 1
        val updated = ingredient.copy(count = mutableIntStateOf(newCount))
        viewModelScope.launch {
            repository.updateIngredient(updated.toEntity())
        }
    }

    fun decreaseCount(ingredient: Ingredient) {
        val current = ingredient.count.value
        if (current > 1) {
            val newCount = current - 1
            val updated = ingredient.copy(count = mutableIntStateOf(newCount))
            viewModelScope.launch {
                repository.updateIngredient(updated.toEntity())
            }
        }
    }

    fun updateIngredientCount(ingredient: Ingredient, newCount: Int) {
        if (newCount > 0) {
            val updated = ingredient.copy(count = mutableIntStateOf(newCount))
            viewModelScope.launch {
                repository.updateIngredient(updated.toEntity())
            }
        }
    }
}
