package com.example.noginger.Feature.RecipeSearch

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.noginger.*
import com.example.noginger.Feature.RecipeList.RecipeListActivity
import com.example.noginger.api.SpoonacularRetriever
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Headers

const val RECIPES = "com.example.noginger.RECIPES"

class RecipeSearchActivity: AppCompatActivity() {

    private val spoonacularRetriever = SpoonacularRetriever()
    private val callback = object : Callback<RecipeResult> {
        override fun onFailure(call: Call<RecipeResult>, t: Throwable) {
            Log.e("MainActivity", "Problem calling Spoonacular API {${t?.message}}")
        }

        override fun onResponse(call: Call<RecipeResult>, response: Response<RecipeResult>) {
            response?.isSuccessful.let {
                val results = response.body()?.results
                val intent = Intent(applicationContext, RecipeListActivity::class.java).apply {
                    putExtra(RECIPES, ArrayList(results))
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_search)

        val excludeIngredientTextView =
            findViewById<TextView>(R.id.excludeIngredientTextView).apply {
                text = "You can't use: ${intent.getStringExtra(CANT_INCLUDE)}"
            }

        val includeIngredientEditText = findViewById<EditText>(R.id.includeIngredientUserInput)
        var searchKeywordEditText = findViewById<EditText>(R.id.otherSearchTermEditText)

        val mealIdeasButton: Button = findViewById(R.id.searchRecipeButton)
        mealIdeasButton.setOnClickListener {
            if (isNetworkConnected()) {
                val intoleranceString = intent.getStringExtra(INTOLERANCE)!!
                val dietString = intent.getStringExtra(DIET)!!
                val cantIncludeSearchString = intent.getStringExtra(CANT_INCLUDE)!!

                val ai: ApplicationInfo = applicationContext.packageManager
                    .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
                val apiKey: String = "${ai.metaData["spoonacularApiKey"]}"

                spoonacularRetriever.getRecipes(callback, apiKey, dietString, intoleranceString, searchKeywordEditText.text.toString(), cantIncludeSearchString, includeIngredientEditText.text.toString())
            } else {
                AlertDialog.Builder(this).setTitle("No internet connection")
                    .setMessage("Please check your internet connection and try again")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert).show()
            }
        }
    }

    private fun showRecipeResultDialog(results: List<Recipe>?) {

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        val recipeResultView = layoutInflater.inflate(R.layout.show_recipe_dialog, null)

        val recipeTitleTextView = recipeResultView.findViewById<TextView>(R.id.titleTextView)
        if (results != null) {
            recipeTitleTextView.setText("You should cook: ${results?.first()?.title} or ${results.get(1).title}")
        }

        dialogBuilder.setView(recipeResultView)
        val dialog = dialogBuilder.create()
        dialog.show()

        val closeButton: Button = recipeResultView.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.hide()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}