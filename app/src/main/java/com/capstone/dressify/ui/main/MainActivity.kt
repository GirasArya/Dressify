package com.capstone.dressify.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityMainBinding
import com.capstone.dressify.ui.camera.CameraActivity
import com.capstone.dressify.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.capstone.dressify.ui.landing.LandingActivity
import com.capstone.dressify.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentImageUri: Uri? = null
    private lateinit var auth: FirebaseAuth

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

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        // Not signed in, launch the Login activity
        if (firebaseUser == null) {
            Toast.makeText(this, "Hayo blom login ya", Toast.LENGTH_SHORT).show()
           startActivity(Intent(this, LandingActivity::class.java))
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
//        launchCameraActivity.launch(intent)
        startActivity(intent)
    }

    private val launchCameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                currentImageUri =
                    result.data?.getParcelableExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
            }
        }
}