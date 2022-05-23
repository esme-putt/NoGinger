package com.example.noginger.api

import com.example.noginger.RecipeResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SpoonacularService {
    @Headers("x-api-key: 39c8a0dbcff14d9aac1c1267626a4693")
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") searchKeyword: String,
        @Query("diet") diet: String,
        @Query("excludeIngredients") excludeIngredients: String,
        @Query("includeIngredients") includeIngredients: String): Call<RecipeResult>
}