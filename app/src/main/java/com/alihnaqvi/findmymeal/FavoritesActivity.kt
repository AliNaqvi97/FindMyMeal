package com.alihnaqvi.findmymeal

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_favorites.*
import com.alihnaqvi.findmymeal.R.string.no_favorites

class FavoritesActivity : AppCompatActivity() {
    private lateinit var dbHelper: MealsDbHelper
    private lateinit var mealsDb: SQLiteDatabase
    private lateinit var meals: ArrayList<Models.Meal>
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        this.title = "Favorite recipes"

        dbHelper = MealsDbHelper(this)
        mealsDb = dbHelper.readableDatabase
        meals = getFavoritedMeals()
        if (meals.isNotEmpty()) {
            empty_view.visibility = View.GONE
            recipesAdapter = RecipesAdapter(this)
            recyclerview_recipes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recipesAdapter.addAll(meals)
            recyclerview_recipes.adapter = recipesAdapter
        } else {
            empty_view.text = getString(no_favorites)
            empty_view.visibility = View.VISIBLE
        }

    }

    private fun getFavoritedMeals(): ArrayList<Models.Meal> {
        val results = ArrayList<Models.Meal>()
        val columns =
            arrayOf(
                BaseColumns._ID,
                MealsDbContract.MealsDbEntry.COLUMN_NAME_ID,
                MealsDbContract.MealsDbEntry.COLUMN_NAME_TITLE,
                MealsDbContract.MealsDbEntry.COLUMN_NAME_URL
            )
        val cursor = mealsDb.query(MealsDbContract.MealsDbEntry.TABLE_NAME, columns, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val mealId = getString(getColumnIndex(MealsDbContract.MealsDbEntry.COLUMN_NAME_ID))
                val thumbnailUrl = getString(getColumnIndex(MealsDbContract.MealsDbEntry.COLUMN_NAME_URL))
                val mealName = getString(getColumnIndex(MealsDbContract.MealsDbEntry.COLUMN_NAME_TITLE))
                results.add(Models.Meal(mealName, thumbnailUrl, mealId))
            }
        }

        cursor.close()
        return results
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val didModify = data?.getBooleanExtra("did_modify", false)!!
            if (didModify) {
                val meals = getFavoritedMeals()
                recipesAdapter.addAll(meals)
                if (meals.isEmpty()) {
                    empty_view.text = getString(no_favorites)
                    empty_view.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
