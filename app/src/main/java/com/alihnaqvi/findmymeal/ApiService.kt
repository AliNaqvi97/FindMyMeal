package com.alihnaqvi.findmymeal

import android.net.Uri
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.StringBuilder

interface ApiService {
    @GET("filter.php")
    fun fetchMealsByArea(@Query("a") area: String): Observable<Models.MealsResult>

    @GET("filter.php")
    fun fetchMealsByCategory(@Query("c") category: String): Observable<Models.MealsResult>

    @GET("lookup.php")
    fun fetchRecipe(@Query("i") id: String): Observable<Models.RecipeResult>

    @GET("json")
    fun fetchRestaurants(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("rankby") rankBy: String,
        @Query("keyword") keyword: String,
        @Query("type") type: String
    ): Observable<Models.RestaurantsResult>

    @GET("json")
    fun fetchGeocodedAddress(
        @Query("key") key: String,
        @Query("place_id") placeId: String
    ): Observable<Models.GeocodingResult>

    companion object {
        const val BASE_MEAL_URI = "https://www.themealdb.com/api/json/v1/1/"
        const val BASE_NEARBY_SEARCH_URI = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
        const val BASE_GEOCODE_URI = "https://maps.googleapis.com/maps/api/geocode/"
        private const val BASE_GOOGLE_URL = "www.google.com"
        private const val SCHEME = "https"
        private const val API_PARAM = "api"
        private const val API = "1"
        private const val QUERY_PARAM = "query"
        private const val PLACE_ID_PARAM = "query_place_id"
        fun createService(baseUrl: String): ApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(ApiService::class.java)
        }

        fun createMapUri(lat: Double, lng: Double, placeId: String): Uri {
            return Uri.Builder()
                .scheme(SCHEME)
                .authority(BASE_GOOGLE_URL)
                .encodedPath("maps/search/")
                .encodedQuery("$API_PARAM=$API&$QUERY_PARAM=${latLngToString(lat, lng)}")
                .appendQueryParameter(PLACE_ID_PARAM, placeId)
                .build()
        }

        fun latLngToString(lat: Double, lng: Double): String {
            return StringBuilder().append(lat).append(",").append(lng).toString()
        }
    }
}