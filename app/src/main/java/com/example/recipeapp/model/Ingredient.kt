
package com.example.recipeapp.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.example.recipeapp.data.IngredientEntity

data class Ingredient(
    val id: Int = 0, // ← 추가
    val name: String,
    val count: MutableState<Int>,
    val expiry: String,
    val memo: String = ""
)

fun IngredientEntity.toUiModel(): Ingredient = Ingredient(
    id = id,
    name = name,
    count = mutableIntStateOf(count),
    expiry = expiry,
    memo = memo
)

fun Ingredient.toEntity(): IngredientEntity = IngredientEntity(
    id = id,
    name = name,
    count = count.value,
    expiry = expiry,
    memo = memo
)

