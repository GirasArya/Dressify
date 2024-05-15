package com.capstone.dressify.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.databinding.ActivityLandingBinding
import com.capstone.dressify.ui.login.LoginActivity
import com.capstone.dressify.ui.register.RegisterActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LandingActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.tvLandingRegister.setOnClickListener{
            val intent = Intent(this@LandingActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}