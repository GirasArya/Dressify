package com.capstone.dressify.ui.view.landing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.dressify.R
import com.capstone.dressify.databinding.ActivityLandingBinding
import com.capstone.dressify.ui.adapter.LandingAdapter
import com.capstone.dressify.ui.view.login.LoginActivity
import me.relex.circleindicator.CircleIndicator3

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private var imageList = listOf<Int>()
    private var titleList = listOf<String>()
    private var descList = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor("#007BFF")

        imageList = listOf(
            R.drawable.img_landing_first,
            R.drawable.img_landing_second,
            R.drawable.img_landing_third,
        )

        titleList = listOf(
            getString(R.string.tv_landingTitle_first),
            getString(R.string.tv_landingTitle_second),
            getString(R.string.tv_landingTitle_third),
        )

        descList = listOf(
            getString(R.string.tv_landingDesc_first),
            getString(R.string.tv_landingDesc_second),
            getString(R.string.tv_landingDesc_third),
        )

        val adapter = LandingAdapter(titleList, descList, imageList)
        binding.vpLanding.adapter = adapter


        val indicator = findViewById<CircleIndicator3>(R.id.vp_dots)
        indicator.setViewPager(binding.vpLanding)



        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LandingActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }
}
