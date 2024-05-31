package com.capstone.dressify.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.dressify.R
import com.capstone.dressify.databinding.FragmentProfileBinding
import com.capstone.dressify.factory.ViewModelFactory
import com.capstone.dressify.ui.view.landing.LandingActivity
import com.capstone.dressify.ui.viewmodel.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var  auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application, requireContext().applicationContext)
    }
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

        loginViewModel.getSession().observe(viewLifecycleOwner) { user ->
            user?.let {
                if (it.isLoggedIn) {
                    binding.tvProfileEmail.text = it.email
                    binding.tvProfileUsername.text = it.username
                    binding.profileLoading.visibility = View.GONE
                }
            }
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
        loginViewModel.logout()
        startActivity(Intent(this@ProfileFragment.requireContext(), LandingActivity::class.java))
    }
}