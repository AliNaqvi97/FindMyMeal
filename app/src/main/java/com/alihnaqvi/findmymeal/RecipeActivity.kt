package com.alihnaqvi.findmymeal

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
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
    private lateinit var param: String
    private lateinit var name: String
    private lateinit var thumbnailUrl: String
    private lateinit var dbHelper: MealsDbHelper
    private lateinit var mealsDb: SQLiteDatabase
    private var didModify: Boolean = false

    private val apiService by lazy { ApiService.createService(ApiService.BASE_MEAL_URI) }
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

        retry_button.setOnClickListener {
            checkInternetConnection()
        }

        name = intent.getStringExtra("title")
        thumbnailUrl = intent.getStringExtra("thumbnail")
        param = intent.getStringExtra("id")

        dbHelper = MealsDbHelper(this)
        mealsDb = dbHelper.writableDatabase

        textview_name.text = name
        this.title = name
        textview_name.isSelected = true
        Picasso.get().load(thumbnailUrl).into(imageview_thumbnail)

        checkInternetConnection()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        if (isFavorited()) {
            val item = menu?.findItem(R.id.button_favorite)
            item?.isChecked = true
            item?.icon = getDrawable(R.drawable.ic_star_gold_48dp)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            R.id.button_favorite -> {
                didModify = didModify xor true
                if (item.isChecked) {
                    item.isChecked = false
                    item.icon = getDrawable(R.drawable.ic_star_border_white_48dp)
                    val selection = "${MealsDbContract.MealsDbEntry.COLUMN_NAME_ID} LIKE ?"
                    val selectionArgs = arrayOf(param)
                    mealsDb.delete(MealsDbContract.MealsDbEntry.TABLE_NAME, selection, selectionArgs)
                    Toast.makeText(this, "Recipe unfavorited", Toast.LENGTH_SHORT).show()
                } else {
                    item.isChecked = true
                    item.icon = getDrawable(R.drawable.ic_star_gold_48dp)
                    val values = ContentValues().apply {
                        put(MealsDbContract.MealsDbEntry.COLUMN_NAME_ID, param)
                        put(MealsDbContract.MealsDbEntry.COLUMN_NAME_TITLE, name)
                        put(MealsDbContract.MealsDbEntry.COLUMN_NAME_URL, thumbnailUrl)
                    }

                    mealsDb.insert(MealsDbContract.MealsDbEntry.TABLE_NAME, null, values)
                    Toast.makeText(this, "Recipe favorited!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMeal(id: String) {
        disposable =
                apiService.fetchRecipe(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result -> showRecipe(result.recipes[0]) },
                        { error ->
                            Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                        })
    }

    override fun onResume() {
        super.onResume()
        progress_circular.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        dbHelper.close()
    }

    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().putExtra("did_modify", didModify))
        super.finish()
    }

    private fun showRecipe(recipe: Models.Recipe) {
        progress_circular.visibility = View.GONE
        instructionsTextView.text = recipe.instructions
        textview_directions.visibility = View.VISIBLE
        textview_ingredients.visibility = View.VISIBLE
        if (recipe.youtubeUrl != null) {
            button_youtube.visibility = View.VISIBLE
            button_youtube.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.youtubeUrl)))
            }
        } else
            button_youtube.visibility = View.GONE

        if (recipe.source != null) {
            button_source.visibility = View.VISIBLE
            button_source.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.source)))
            }
        }
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

    private fun checkInternetConnection() {
        progress_circular.visibility = View.VISIBLE
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            empty_view.visibility = View.GONE
            retry_button.visibility = View.GONE
            getMeal(param)
        } else {
            progress_circular.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
            retry_button.visibility = View.VISIBLE
        }
    }

    private fun isFavorited(): Boolean {
        val columns =
            arrayOf(
                BaseColumns._ID,
                MealsDbContract.MealsDbEntry.COLUMN_NAME_ID,
                MealsDbContract.MealsDbEntry.COLUMN_NAME_TITLE,
                MealsDbContract.MealsDbEntry.COLUMN_NAME_URL
            )
        val selection = "${MealsDbContract.MealsDbEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(param)
        val cursor =
            mealsDb.query(MealsDbContract.MealsDbEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }
}
