package com.capstone.dressify.ui.view.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.dressify.R
import com.capstone.dressify.data.local.datastore.UserPreference
import com.capstone.dressify.data.local.datastore.dataStore
import com.capstone.dressify.data.remote.response.LoginResponse
import com.capstone.dressify.databinding.ActivityLoginBinding
import com.capstone.dressify.domain.model.User
import com.capstone.dressify.factory.ViewModelFactory
import com.capstone.dressify.ui.view.main.MainActivity
import com.capstone.dressify.ui.view.recommendation.RecommendationActivity
import com.capstone.dressify.ui.view.register.RegisterActivity
import com.capstone.dressify.ui.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class  LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(application, applicationContext)
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = UserPreference(applicationContext.dataStore)
        val session = loginViewModel.getSession().value
        changeStatusBarColor("#007BFF")

        //Toolbar
        setSupportActionBar(findViewById(R.id.tb_login))
        setTitle(null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        supportActionBar?.elevation = 0f

        //tv to register
        binding.tvLoginToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //Observe login response and save session
        loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
            if (response.error == false) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RecommendationActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
            }

            saveSession(
                User(
                    username = response.loginResult?.username.toString(),
                    email = response.loginResult?.email.toString(),
                    token = AUTH_KEY + response.loginResult?.token.toString(),
                    isLoggedIn = true
                )
            )

        }

        //get email and password value
        binding.btnLogin.setOnClickListener {
            if (isInputValid()) {
                val email = binding.edtEmailLogin.text.toString().trim()
                val password = binding.edtPasswordLogin.text.toString().trim()
//            binding.progressBarLogin.visibility = View.VISIBLE
                lifecycleScope.launch {
                    try {
                        val message = loginViewModel.login(email, password)
                        Log.d(message.toString(), "message : ")
                        loginViewModel.loginSession()
                    } catch (e : HttpException){
                        Toast.makeText(this@LoginActivity, "Login failed : ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        //Firebase Login
        binding.ivGoogleLogin.setOnClickListener {
            signIn()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // ignore unresolved reference
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth


        if (session != null && session.isLoggedIn) {
            moveActivity()
            return
        }
    }

    private fun moveActivity() {
        val intent = Intent(this@LoginActivity, RecommendationActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, RecommendationActivity::class.java))
            finish()
        }
    }

    //Save datastore session
    private fun saveSession(user: User) {
        lifecycleScope.launch {
            loginViewModel.saveSession(user)
        }
        Log.d("SESSION", "Login session saved")
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val AUTH_KEY = " "
    }

    private fun isInputValid(): Boolean {
        var isValid = true
        if (binding.edtEmailLogin.text.toString().trim().isEmpty()) {
            binding.edtEmailLogin.error = "Email is required"
            isValid = false
        } else {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!binding.edtEmailLogin.text.toString().trim().matches(emailPattern.toRegex())) {
                binding.edtEmailLogin.error = "Invalid email format"
                isValid = false
            }
        }

        if (binding.edtPasswordLogin.text.toString().trim().isEmpty()) {
            binding.edtPasswordLogin.setError("Passwords is required", null)
            isValid = false
        } else {
            binding.edtPasswordLogin.error = null
        }

        return isValid
    }

    private fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }
}