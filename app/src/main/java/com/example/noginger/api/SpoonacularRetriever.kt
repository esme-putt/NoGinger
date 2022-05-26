package com.example.noginger.api

import com.example.noginger.RecipeResult
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpoonacularRetriever {
    private val service: SpoonacularService

    companion object {
        const val BASE_URL = "https://api.spoonacular.com/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(SpoonacularService::class.java)
    }

    fun getRecipes(callback: Callback<RecipeResult>,
                   apiKey: String = "",
                   diet: String = "",
                   intolerances: String = "",
                   searchKeyword: String = "",
                   excludeIngredients: String = "",
                   includeIngredients: String = "") {
        val call = service.searchRecipes(
            apiKey = apiKey,
            intolerances = intolerances,
            diet = diet,
            searchKeyword = searchKeyword,
            includeIngredients = includeIngredients,
            excludeIngredients = excludeIngredients)
        call.enqueue(callback)
    }
}