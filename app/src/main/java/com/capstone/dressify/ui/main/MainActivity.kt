package com.capstone.dressify.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_catalog -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_fragment, CatalogFragment())
                        .commit()
                    binding.tvCatalogTitle.visibility = View.VISIBLE
                }

                R.id.nav_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_fragment, FavouriteFragment())
                        .commit()
                    binding.tvCatalogTitle.visibility = View.GONE
                }

                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_fragment, ProfileFragment())
                        .commit()
                    binding.tvCatalogTitle.visibility = View.GONE
                }
            }
            true
        }
    }
}