package com.example.noginger.Feature.RecipeList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noginger.Feature.RecipeSearch.RECIPES
import com.example.noginger.R
import com.example.noginger.Recipe

lateinit var adapter: RecipesAdapter
lateinit var recipesList: RecyclerView

class RecipeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recipesList = findViewById<View>(R.id.recipes_list) as RecyclerView
        adapter = RecipesAdapter(emptyList())

        adapter.recipes = intent.getParcelableArrayListExtra<Recipe>(RECIPES)!!

        recipesList.adapter = adapter
        recipesList.layoutManager = LinearLayoutManager(this)
    }
}