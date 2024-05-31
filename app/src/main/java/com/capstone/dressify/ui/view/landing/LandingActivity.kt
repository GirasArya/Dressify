package com.capstone.dressify.ui.view.landing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.databinding.ActivityLandingBinding
import com.capstone.dressify.ui.view.login.LoginActivity
import com.capstone.dressify.ui.view.register.RegisterActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor("#007BFF")

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LandingActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.tvLandingRegister.setOnClickListener{
            val intent = Intent(this@LandingActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }
}