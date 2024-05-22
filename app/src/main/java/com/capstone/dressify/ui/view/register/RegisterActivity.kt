package com.capstone.dressify.ui.view.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityRegisterBinding
import com.capstone.dressify.ui.view.landing.LandingActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Toolbar
        setSupportActionBar(findViewById(R.id.tb_register))
        setTitle(null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar?.elevation = 0f

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }
    }
}