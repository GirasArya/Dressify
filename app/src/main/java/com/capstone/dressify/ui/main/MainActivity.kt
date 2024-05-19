package com.capstone.dressify.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityMainBinding
import com.capstone.dressify.ui.camera.CameraActivity
import com.capstone.dressify.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView

        binding.tvCatalogTitle.setOnClickListener {
            startCameraX()
        }

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

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
//        launchCameraActivity.launch(intent)
        startActivity(intent)
    }

    private val launchCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            currentImageUri = result.data?.getParcelableExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
        }
    }
}