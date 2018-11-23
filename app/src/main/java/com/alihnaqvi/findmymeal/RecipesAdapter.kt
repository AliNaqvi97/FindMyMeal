package com.alihnaqvi.findmymeal

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v4.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_recipes.view.*


class RecipesAdapter(private val activity: Activity) : RecyclerView.Adapter<RecipesAdapter.RecipeHolder>() {
    private val recipes = ArrayList<Models.Meal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_recipes, parent, false)
        return RecipeHolder(view)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = recipes[holder.adapterPosition]
        Picasso.get().load(recipe.thumbnailUrl).into(holder.thumbnailImageView)
        holder.titleTextView.text = recipe.title

        holder.mealCardView.setOnClickListener {
            val intent = Intent(activity, RecipeActivity::class.java)
            intent.putExtra("thumbnail", recipe.thumbnailUrl)
            intent.putExtra("id", recipe.id)
            intent.putExtra("title", recipe.title)
            val thumbnail = Pair.create(holder.thumbnailImageView as View, "thumbnail")
            val title = Pair.create(holder.titleTextView as View, "title")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, thumbnail, title)
            activity.startActivity(intent, options.toBundle())
        }
    }

    fun addAll(data: ArrayList<Models.Meal>) {
        recipes.clear()
        recipes.addAll(data)
        notifyDataSetChanged()
    }

    class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.imageview_thumbnail
        val titleTextView: TextView = itemView.textview_title
        val mealCardView: CardView = itemView.cardview_meal
    }
}