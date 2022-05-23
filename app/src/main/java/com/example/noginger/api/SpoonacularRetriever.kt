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
                   diet: String = "",
                   searchKeyword: String = "",
                   excludeIngredients: String = "",
                   includeIngredients: String = "") {
        val call = service.searchRecipes(
            searchKeyword = searchKeyword,
            diet = diet,
            includeIngredients = includeIngredients,
            excludeIngredients = excludeIngredients)
        call.enqueue(callback)
    }

//    override fun getOkHttpClientBuilder(): OkHttpClient.Builder {
//        val okHttpBuilder = super.getOkHttpClientBuilder()
//        okHttpBuilder.addInterceptor { chain ->
//            val request = chain.request().newBuilder()
//            val originalHttpUrl = chain.request().url
//            val url = originalHttpUrl.newBuilder().addQueryParameter("api_key", "your api key value").build()
//            request.url(url)
//            val response = chain.proceed(request.build())
//            return@addInterceptor response
//        }
//        return okHttpBuilder
//    }
}