package com.example.noginger.Feature.RecipeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noginger.R
import com.example.noginger.Recipe

class RecipesAdapter(private val mRecipes: List<Recipe>): RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    var recipes = mRecipes
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.recipe_image)
        val titleTextView = itemView.findViewById<TextView>(R.id.recipe_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val memberView = inflater.inflate(R.layout.recipe_row, parent, false)
        return ViewHolder(memberView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe: Recipe = recipes[position]

        val imageView = holder.imageView
        Glide.with(imageView)
            .load(recipe.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_fastfood_24)
            .error(R.drawable.ic_baseline_fastfood_24)
            .into(imageView)

        val titleTextView = holder.titleTextView
        titleTextView.setText(recipe.title)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}