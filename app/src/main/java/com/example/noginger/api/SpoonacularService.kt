package com.example.noginger.api

import com.example.noginger.RecipeResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularService {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("intolerances") intolerances: String,
        @Query("query") searchKeyword: String,
        @Query("diet") diet: String,
        @Query("excludeIngredients") excludeIngredients: String,
        @Query("includeIngredients") includeIngredients: String): Call<RecipeResult>
}