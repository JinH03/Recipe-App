// Mapper.kt (data 패키지 안)

package com.example.recipeapp.data

import com.example.recipeapp.model.Ingredient
import com.example.recipeapp.data.IngredientEntity
import androidx.compose.runtime.mutableIntStateOf

fun IngredientEntity.toUiModel(): Ingredient = Ingredient(
    name = this.name,
    count = mutableIntStateOf(this.count),
    expiry = this.expiry,
    memo = this.memo
)

fun Ingredient.toEntity(): IngredientEntity = IngredientEntity(
    name = this.name,
    count = this.count.value,
    expiry = this.expiry,
    memo = this.memo
)
