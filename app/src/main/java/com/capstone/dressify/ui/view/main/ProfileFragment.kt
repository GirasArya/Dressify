package com.capstone.dressify.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.dressify.R
import com.capstone.dressify.databinding.FragmentProfileBinding
import com.capstone.dressify.ui.view.landing.LandingActivity
import com.capstone.dressify.ui.view.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener {
            signOut()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        auth = Firebase.auth // Initialize auth here
        val firebaseUser = auth.currentUser
        binding.profileLoading.visibility = View.VISIBLE
        if (firebaseUser != null) {
            val email = firebaseUser.email
            val displayName = firebaseUser.displayName
            val profileUser = firebaseUser.photoUrl


            Glide.with(this)
                .load(profileUser)
                .placeholder(R.drawable.ic_avatar_profile)
                .circleCrop()
                .into(binding.ivAvatarProfile)

            binding.tvProfileEmail.text = email ?: "Email not available"
            binding.tvProfileUsername.text = displayName ?: "Username not available"
            binding.profileLoading.visibility = View.GONE
        }
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this@ProfileFragment.requireContext(), LandingActivity::class.java))
    }
}