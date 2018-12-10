package com.alihnaqvi.findmymeal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var infoTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var rollButton: Button
    private lateinit var findButton: Button

    private var category: String? = null
    private var randomCategory = 0

    private val CATEGORIES = arrayOf(
        "American",
        "British",
        "Canadian",
        "Chinese",
        "French",
        "Greek",
        "Indian",
        "Italian",
        "Jamaican",
        "Japanese",
        "Malaysian",
        "Mexican",
        "Moroccan",
        "Seafood",
        "Spanish",
        "Thai",
        "Vegan",
        "Vegetarian",
        "Vietnamese"
    )

    private val rollListener = View.OnClickListener {
        randomCategory = (Math.random() * CATEGORIES.size).toInt()
        category = CATEGORIES[randomCategory]
        if (!findButton.isEnabled) findButton.isEnabled = true

        updateUi()
    }

    private val findMealListener = View.OnClickListener {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infoTextView = textview_info
        categoryTextView = textview_category
        rollButton = button_roll
        findButton = button_find

        infoTextView.text = getString(R.string.category_unrolled)
        categoryTextView.text = "?????????"

        rollButton.setOnClickListener(rollListener)
        findButton.setOnClickListener(findMealListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorites, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.open_favorites -> startActivity(Intent(this, FavoritesActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUi() {
        infoTextView.text = getString(R.string.category_rolled)
        categoryTextView.text = category?.toUpperCase()
    }
}
