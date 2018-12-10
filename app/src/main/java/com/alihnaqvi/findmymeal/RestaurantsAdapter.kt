package com.alihnaqvi.findmymeal

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_restaurants.view.*

class RestaurantsAdapter(private val context: Context) : RecyclerView.Adapter<RestaurantsAdapter.RestaurantsHolder>() {
    private val restaurants = ArrayList<Models.Restaurant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_restaurants, parent, false)
        return RestaurantsHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantsAdapter.RestaurantsHolder, position: Int) {
        val restaurant = restaurants[holder.adapterPosition]

        holder.nameTextView.text = restaurant.name
        holder.addressTextView.text = restaurant.address

        if (restaurant != null && restaurant.hours != null && restaurant.hours.openNow) {
            holder.openTextView.text = context.getString(R.string.open)
            holder.openTextView.setTextColor(ContextCompat.getColor(context, R.color.colorOpen))
        } else {
            holder.openTextView.text = context.getString(R.string.closed)
            holder.openTextView.setTextColor(ContextCompat.getColor(context, R.color.colorClosed))
        }

        holder.ratingTextView.text = restaurant.rating.toString()
        holder.ratingBar.rating = restaurant.rating

        holder.restaurantCardView.setOnClickListener {
            val mapUri = ApiService.createMapUri(
                restaurant.geometry.location.lat,
                restaurant.geometry.location.lng,
                restaurant.placeId)

            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                context.startActivity(mapIntent)
        }
    }

    fun addAll(data: ArrayList<Models.Restaurant>) {
        restaurants.clear()
        restaurants.addAll(data)
        notifyDataSetChanged()
    }

    class RestaurantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantCardView: CardView = itemView.cardview_meal
        val nameTextView: TextView = itemView.textview_name
        val addressTextView: TextView = itemView.textview_address
        val openTextView: TextView = itemView.textview_open
        val ratingTextView: TextView = itemView.textview_rating
        val ratingBar: RatingBar = itemView.ratingBar
    }
}