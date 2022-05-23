package com.example.noginger

data class RecipeResult(val results: List<Recipe>)

data class Recipe(
    val id: Long?,
    val title: String?,
    val image: String?,
    val sourceUrl: String?
)