package com.capstone.dressify.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.databinding.ActivityLoginBinding
import com.capstone.dressify.R
import com.capstone.dressify.ui.main.MainActivity
import com.capstone.dressify.ui.recommendation.RecommendationActivity
import com.capstone.dressify.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Toolbar
        setSupportActionBar(findViewById(R.id.tb_login))
        setTitle(null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar?.elevation = 0f

        binding.tvLoginToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, RecommendationActivity::class.java)
            startActivity(intent)
        }

        binding.ivGoogleLogin.setOnClickListener {
            Toast.makeText(this, "Fitur belum selesai dibuat", Toast.LENGTH_SHORT).show()
        }
    }
}