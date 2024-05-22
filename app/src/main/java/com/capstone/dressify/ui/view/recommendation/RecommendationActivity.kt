package com.capstone.dressify.ui.view.recommendation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityRecommendationBinding
import com.capstone.dressify.ui.view.main.MainActivity

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSubmit.setOnClickListener {
            val intent = Intent(this@RecommendationActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}