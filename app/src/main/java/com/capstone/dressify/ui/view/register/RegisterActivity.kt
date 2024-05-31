package com.capstone.dressify.ui.view.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.dressify.R
import com.capstone.dressify.data.remote.response.RegisterResponse
import com.capstone.dressify.databinding.ActivityRegisterBinding
import com.capstone.dressify.factory.ViewModelFactory
import com.capstone.dressify.ui.view.login.LoginActivity
import com.capstone.dressify.ui.viewmodel.RegisterViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(application, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor("#007BFF")

        //Toolbar
        setSupportActionBar(findViewById(R.id.tb_register))
        setTitle(null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar?.elevation = 0f

        val edtPassword = binding.edtRegisterPassword
        val edtConfirmPassword = binding.edtRegisterConfirmPassword

        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                edtConfirmPassword.setPasswordToMatch(s.toString())
            }

        })

        binding.btnRegister.setOnClickListener {
            val email = binding.edtRegisterEmail.text.toString().trim()
            val username = binding.edtRegisterUsername.text.toString().trim()
            val password = binding.edtRegisterPassword.text.toString().trim()

            lifecycleScope.launch {
                try {
                    val message = registerViewModel.registerUser(email, username, password)
                    Log.d(message.toString(), "message : ")
                } catch (e: HttpException) {
                    Toast.makeText(this@RegisterActivity, "Register failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerViewModel.registerResponse.observe(this) { response ->
            if (response.error == false) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Registration failed: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }

}