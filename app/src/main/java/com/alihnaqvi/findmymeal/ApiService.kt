package com.alihnaqvi.findmymeal

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("filter.php")
    fun fetchMealsByArea(@Query("a") area: String): Observable<Models.MealsResult>

    @GET("filter.php")
    fun fetchMealsByCategory(@Query("c") category: String): Observable<Models.MealsResult>

    @GET("lookup.php")
    fun fetchRecipe(@Query("i") id: String): Observable<Models.RecipeResult>

    companion object {
        fun createService(): ApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}