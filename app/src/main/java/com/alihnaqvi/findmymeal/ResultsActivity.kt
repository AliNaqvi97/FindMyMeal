package com.alihnaqvi.findmymeal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_results.*


class ResultsActivity : AppCompatActivity() {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var param: String

    private val apiService by lazy { ApiService.createService() }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        param = intent.getStringExtra("category")
        resultsRecyclerView = recyclerview_results
        resultsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recipesAdapter = RecipesAdapter(this)
        resultsRecyclerView.adapter = recipesAdapter

        this.title = param

        getMeals(param)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMeals(category: String) {
        val observable =
            if (category == "Seafood") apiService.fetchMealsByCategory(category)
            else apiService.fetchMealsByArea(category)

        disposable =
                observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result -> showMeals(result.meals) },
                        { error ->
                            Toast.makeText(this, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                        })
    }

    private fun showMeals(meals: ArrayList<Models.Meal>) {
        progress_circular.visibility = View.GONE
        recipesAdapter.addAll(meals)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
