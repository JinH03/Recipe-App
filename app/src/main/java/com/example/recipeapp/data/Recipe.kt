package com.example.recipeapp.data

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val ingredients: List<String>,
    val instructions: String,
    val likes: Int = 0,
    var isFavorite: Boolean = false
)