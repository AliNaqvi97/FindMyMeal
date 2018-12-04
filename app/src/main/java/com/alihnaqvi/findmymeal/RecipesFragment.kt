package com.alihnaqvi.findmymeal

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*

class RecipesFragment() : Fragment() {
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var param: String

    private val apiService by lazy { ApiService.createService() }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        resultsRecyclerView = view.recyclerview_recipes
        resultsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        param = activity!!.intent.getStringExtra("category")
        recipesAdapter = RecipesAdapter(activity as Activity)
        resultsRecyclerView.adapter = recipesAdapter
        getMeals(param)
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
                            Toast.makeText(activity, "Error: " + error.message, Toast.LENGTH_SHORT).show()
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