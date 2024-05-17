package com.capstone.dressify.ui.camera

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityCameraBinding
import com.capstone.dressify.ui.main.MainActivity

class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flBackArrowCamera.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}