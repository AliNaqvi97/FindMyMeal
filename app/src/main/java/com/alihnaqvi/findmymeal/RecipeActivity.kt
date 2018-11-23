package com.alihnaqvi.findmymeal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : AppCompatActivity() {
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var instructionsTextView: TextView

    private val apiService by lazy { ApiService.createService() }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        instructionsTextView = textview_instructions
        ingredientsAdapter = IngredientsAdapter(this)
        ingredientsRecyclerView = recyclerview_ingredients
        ingredientsRecyclerView.isNestedScrollingEnabled = false
        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ingredientsRecyclerView.adapter = ingredientsAdapter

        val title = intent.getStringExtra("title")
        val thumbnailUrl = intent.getStringExtra("thumbnail")

        textview_title.text = title
        textview_title.isSelected = true
        Picasso.get().load(thumbnailUrl).into(imageview_thumbnail)

        getMeals(intent.getStringExtra("id"))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMeals(id: String) {
        disposable =
                apiService.fetchRecipe(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result -> showRecipe(result.meals[0]) },
                        { error ->
                            Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun showRecipe(recipe: Models.Recipe) {
        progress_circular.visibility = View.GONE
        instructionsTextView.text = recipe.instructions
        textview_directions.visibility = View.VISIBLE
        textview_ingredients.visibility = View.VISIBLE
        val ingredients = getIngredients(recipe)
        val measures = getMeasures(recipe)
        ingredientsAdapter.addAll(ingredients, measures)
    }

    private fun getIngredients(recipe: Models.Recipe): ArrayList<String?> {
        val ingredients = ArrayList<String?>()
        ingredients.add(recipe.ingredient1)
        ingredients.add(recipe.ingredient2)
        ingredients.add(recipe.ingredient3)
        ingredients.add(recipe.ingredient4)
        ingredients.add(recipe.ingredient5)
        ingredients.add(recipe.ingredient6)
        ingredients.add(recipe.ingredient7)
        ingredients.add(recipe.ingredient8)
        ingredients.add(recipe.ingredient9)
        ingredients.add(recipe.ingredient10)
        ingredients.add(recipe.ingredient11)
        ingredients.add(recipe.ingredient12)
        ingredients.add(recipe.ingredient13)
        ingredients.add(recipe.ingredient14)
        ingredients.add(recipe.ingredient15)
        ingredients.add(recipe.ingredient16)
        ingredients.add(recipe.ingredient17)
        ingredients.add(recipe.ingredient18)
        ingredients.add(recipe.ingredient19)
        ingredients.add(recipe.ingredient20)
        return ingredients.filter { it != null && it != "" } as ArrayList<String?>
    }

    private fun getMeasures(recipe: Models.Recipe): ArrayList<String?> {
        val measures = ArrayList<String?>()
        measures.add(recipe.measure1)
        measures.add(recipe.measure2)
        measures.add(recipe.measure3)
        measures.add(recipe.measure4)
        measures.add(recipe.measure5)
        measures.add(recipe.measure6)
        measures.add(recipe.measure7)
        measures.add(recipe.measure8)
        measures.add(recipe.measure9)
        measures.add(recipe.measure10)
        measures.add(recipe.measure11)
        measures.add(recipe.measure12)
        measures.add(recipe.measure13)
        measures.add(recipe.measure14)
        measures.add(recipe.measure15)
        measures.add(recipe.measure16)
        measures.add(recipe.measure17)
        measures.add(recipe.measure18)
        measures.add(recipe.measure19)
        measures.add(recipe.measure20)
        return measures.filter { it != null && it != "" } as ArrayList<String?>
    }
}
