package com.alihnaqvi.findmymeal

import com.google.gson.annotations.SerializedName

object Models {
    data class MealsResult(@SerializedName("meals") val meals: ArrayList<Meal>)

    data class RecipeResult(@SerializedName("meals") val recipes: ArrayList<Recipe>)

    data class RestaurantsResult(@SerializedName("results") val restaurants: ArrayList<Restaurant>)

    data class GeocodingResult(@SerializedName("results") val addresses: ArrayList<Address>)

    data class Meal(
        @SerializedName("strMeal") val title: String,
        @SerializedName("strMealThumb") val thumbnailUrl: String,
        @SerializedName("idMeal") val id: String
    )

    data class Recipe(
        @SerializedName("strInstructions") val instructions: String,
        @SerializedName("strYoutube") val youtubeUrl: String?,
        @SerializedName("strIngredient1") val ingredient1: String?,
        @SerializedName("strIngredient2") val ingredient2: String?,
        @SerializedName("strIngredient3") val ingredient3: String?,
        @SerializedName("strIngredient4") val ingredient4: String?,
        @SerializedName("strIngredient5") val ingredient5: String?,
        @SerializedName("strIngredient6") val ingredient6: String?,
        @SerializedName("strIngredient7") val ingredient7: String?,
        @SerializedName("strIngredient8") val ingredient8: String?,
        @SerializedName("strIngredient9") val ingredient9: String?,
        @SerializedName("strIngredient10") val ingredient10: String?,
        @SerializedName("strIngredient11") val ingredient11: String?,
        @SerializedName("strIngredient12") val ingredient12: String?,
        @SerializedName("strIngredient13") val ingredient13: String?,
        @SerializedName("strIngredient14") val ingredient14: String?,
        @SerializedName("strIngredient15") val ingredient15: String?,
        @SerializedName("strIngredient16") val ingredient16: String?,
        @SerializedName("strIngredient17") val ingredient17: String?,
        @SerializedName("strIngredient18") val ingredient18: String?,
        @SerializedName("strIngredient19") val ingredient19: String?,
        @SerializedName("strIngredient20") val ingredient20: String?,
        @SerializedName("strMeasure1") val measure1: String?,
        @SerializedName("strMeasure2") val measure2: String?,
        @SerializedName("strMeasure3") val measure3: String?,
        @SerializedName("strMeasure4") val measure4: String?,
        @SerializedName("strMeasure5") val measure5: String?,
        @SerializedName("strMeasure6") val measure6: String?,
        @SerializedName("strMeasure7") val measure7: String?,
        @SerializedName("strMeasure8") val measure8: String?,
        @SerializedName("strMeasure9") val measure9: String?,
        @SerializedName("strMeasure10") val measure10: String?,
        @SerializedName("strMeasure11") val measure11: String?,
        @SerializedName("strMeasure13") val measure13: String?,
        @SerializedName("strMeasure14") val measure14: String?,
        @SerializedName("strMeasure15") val measure15: String?,
        @SerializedName("strMeasure16") val measure16: String?,
        @SerializedName("strMeasure17") val measure17: String?,
        @SerializedName("strMeasure12") val measure12: String?,
        @SerializedName("strMeasure18") val measure18: String?,
        @SerializedName("strMeasure19") val measure19: String?,
        @SerializedName("strMeasure20") val measure20: String?,
        @SerializedName("strSource") val source: String?
    )

    data class Restaurant(
        @SerializedName("geometry") val geometry: Geometry,
        @SerializedName("name") val name: String,
        @SerializedName("opening_hours") val hours: Hours,
        @SerializedName("place_id") val placeId: String,
        @SerializedName("rating") val rating: Float,
        var address: String
    ) {

        data class Geometry(@SerializedName("location") val location: Location) {

            data class Location(
                @SerializedName("lat") val lat: Double,
                @SerializedName("lng") val lng: Double
            )
        }

        data class Hours(@SerializedName("open_now") val openNow: Boolean)
    }

    data class Address(@SerializedName("formatted_address") val address: String)
}