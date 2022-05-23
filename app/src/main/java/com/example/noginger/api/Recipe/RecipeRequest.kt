package com.example.noginger.api.Recipe

import com.example.noginger.RecipeResult
import com.google.gson.Gson
import java.net.URL

class RecipeRequest {

    companion object {
        private const val URL = ""
        private const val SEARCH = ""
        private const val COMPLETE_URL = "$URL?$SEARCH"
    }

    fun run(): RecipeResult {
        val recipeListJsonStr = URL(COMPLETE_URL).readText()
        return Gson().fromJson(recipeListJsonStr, RecipeResult::class.java)
    }
}