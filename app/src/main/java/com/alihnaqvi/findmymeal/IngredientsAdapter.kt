package com.alihnaqvi.findmymeal

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_ingredients.view.*

class IngredientsAdapter(private val context: Context) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {
    private val ingredients = ArrayList<String?>()
    private val measures = ArrayList<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_ingredients, parent, false)
        return IngredientsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val ingredient = ingredients[holder.adapterPosition]
        val measure = measures[holder.adapterPosition]

        holder.ingredientTextView.text = ingredient
        holder.measureTextView.text = measure
    }

    fun addAll(ingredients: ArrayList<String?>, measures: ArrayList<String?>) {
        this.ingredients.clear()
        this.measures.clear()
        this.ingredients.addAll(ingredients)
        this.measures.addAll(measures)
        notifyDataSetChanged()
    }

    class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientTextView: TextView = itemView.textview_ingredient
        val measureTextView: TextView = itemView.textview_measure
    }
}