package com.alihnaqvi.findmymeal

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        this.title = intent.getStringExtra("category")

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewpager)
        supportActionBar?.elevation = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class PagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        private val TITLES = arrayOf("Recipes", "Restaurants")

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> RecipesFragment()
                1 -> RestaurantsFragment()
                else -> RecipesFragment()
            }
        }

        override fun getCount(): Int {
            return TITLES.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return TITLES[position]
        }
    }
}
