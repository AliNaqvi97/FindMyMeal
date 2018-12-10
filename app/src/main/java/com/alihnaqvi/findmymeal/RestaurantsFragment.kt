package com.alihnaqvi.findmymeal

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_restaurants.*
import kotlinx.android.synthetic.main.fragment_restaurants.view.*
import com.alihnaqvi.findmymeal.R.string.goole_maps_api

class RestaurantsFragment : Fragment() {
    private lateinit var restaurantsRecyclerView: RecyclerView
    private lateinit var restaurantsAdapter: RestaurantsAdapter
    private lateinit var param: String
    private var location: String? = null

    private val searchApiService by lazy { ApiService.createService(ApiService.BASE_NEARBY_SEARCH_URI) }
    private val geocodeApiService by lazy { ApiService.createService(ApiService.BASE_GEOCODE_URI) }
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)
        restaurantsRecyclerView = view.recyclerview_restaurants
        restaurantsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retry_button.setOnClickListener {
            checkInternetConnection()
        }

        param = activity!!.intent.getStringExtra("category")
        restaurantsAdapter = RestaurantsAdapter(activity as Activity)
        restaurantsRecyclerView.adapter = restaurantsAdapter
        getLocation()
    }

    private fun checkInternetConnection() {
        progress_circular.visibility = View.VISIBLE
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            empty_view.visibility = View.GONE
            retry_button.visibility = View.GONE
            getRestaurants(param)
        } else {
            progress_circular.visibility = View.GONE
            empty_view.text = getString(R.string.no_internet_connection)
            empty_view.visibility = View.VISIBLE
            retry_button.visibility = View.VISIBLE
        }
    }

    private fun getRestaurants(category: String) {
        if (location != null) {
            val observable =
                searchApiService.fetchRestaurants(
                    context!!.getString(goole_maps_api),
                    location as String,
                    "distance", category,
                    "restaurant"
                )

            disposable =
                    observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { result -> convertLatLngToAddress(result.restaurants) },
                            { error ->
                                Toast.makeText(activity, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                            })
        } else {
            Toast.makeText(activity, "Please turn on location services and try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRestaurants(restaurants: ArrayList<Models.Restaurant>) {
        progress_circular.visibility = View.GONE
        restaurantsAdapter.addAll(restaurants)
    }

    private fun convertLatLngToAddress(restaurants: ArrayList<Models.Restaurant>) {
        for (restaurant in restaurants) {
            val observable = geocodeApiService.fetchGeocodedAddress(context!!.getString(goole_maps_api), restaurant.placeId)

            disposable =
                    observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { result ->
                                restaurant.address = result.addresses[0].address
                                restaurantsAdapter.notifyDataSetChanged()
                            },
                            { error ->
                                Log.d("Restaurants", "Error: " + error.message)
                            })
        }
        showRestaurants(restaurants)
    }

    private fun getLocation() {
        val context = context as Context
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    setLocation(ApiService.latLngToString(it.latitude, it.longitude))
                    checkInternetConnection()
                } else {
                    displayLocationMessage()
                }
            }
        } else {
            setLocation(null)
            Toast.makeText(activity, "Please allow location permissions and try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayLocationMessage() {
        progress_circular.visibility = View.GONE
        empty_view.text = "Location services turned off.\nPlease turn on and try again later."
        empty_view.visibility = View.VISIBLE
    }
    private fun setLocation(location: String?) {
        this.location = location
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}