package com.capstone.dressify.ui.view.main

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityMainBinding
import com.capstone.dressify.factory.ViewModelFactory
import com.capstone.dressify.ui.view.camera.CameraActivity
import com.capstone.dressify.ui.view.landing.LandingActivity
import com.capstone.dressify.ui.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentImageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(application, applicationContext)
    }
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor("#007BFF")

        bottomNavigationView = binding.bottomNavigationView

        binding.tvCatalogTitle.setOnClickListener {
            startCameraX()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_fragment, CatalogFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_fragment, CatalogFragment())
            .commit()

        @Suppress("DEPRECATION")
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

        loginViewModel.getSession().observe(this) { user ->
            user?.let {
                if (it.isLoggedIn || firebaseUser != null) {
                    //get session token of firebase user and save it into local datastore
                    if (firebaseUser != null) {
                        firebaseUser.getIdToken(true)
                            .addOnSuccessListener { result ->
                                token = result.token!!
                                Log.d("FirebaseToken", "Token: $token")
                            }
                    } else {
                        //get session token from local datastore
                        token = it.token
                        Log.d("MainActivity", "Token: $token")
                    }

                } else {
                    val intent = Intent(this@MainActivity, LandingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
//        launchCameraActivity.launch(intent)
        startActivity(intent)
    }

//    private val launchCameraActivity =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RES qULT_OK) {
//                currentImageUri =
//                    result.data?.getParcelableExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
//            }
//        }

    private fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }
}