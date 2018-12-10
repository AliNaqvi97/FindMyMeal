package com.alihnaqvi.findmymeal

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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

class RecipesFragment : Fragment() {
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var param: String

    private val apiService by lazy { ApiService.createService(ApiService.BASE_MEAL_URI) }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        recipesRecyclerView = view.recyclerview_recipes
        recipesRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retry_button.setOnClickListener {
            checkInternetConnection()
        }

        param = activity!!.intent.getStringExtra("category")
        recipesAdapter = RecipesAdapter(activity as Activity)
        recipesRecyclerView.adapter = recipesAdapter

        checkInternetConnection()
    }

    private fun getMeals(category: String) {
        val observable = when (category) {
            "Seafood", "Vegan", "Vegetarian" -> apiService.fetchMealsByCategory(category)
            else -> apiService.fetchMealsByArea(category)
        }

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

    private fun checkInternetConnection() {
        progress_circular.visibility = View.VISIBLE
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            empty_view.visibility = View.GONE
            retry_button.visibility = View.GONE
            getMeals(param)
        } else {
            progress_circular.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
            retry_button.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}