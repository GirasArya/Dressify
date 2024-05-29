package com.capstone.dressify.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.dressify.databinding.FragmentFavouriteBinding
import com.capstone.dressify.factory.ViewModelFactory
import com.capstone.dressify.ui.adapter.FavoriteAdapter
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteAdapter

    private val favViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireActivity().application, requireContext().applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        favViewModel.getAllFavorite().observe(viewLifecycleOwner) { products ->
            if (products != null) {
                adapter.setListAdapter(products)
                adapter.favoriteViewModel = favViewModel
            }

            binding?.tvNoFavorites?.visibility =
                if (products.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding?.rvListFavorite?.visibility =
                if (products.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        adapter = FavoriteAdapter()
        binding?.rvListFavorite?.layoutManager = GridLayoutManager(requireContext(), 2)
        binding?.rvListFavorite?.setHasFixedSize(true)
        binding?.rvListFavorite?.adapter = adapter


        favViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding?.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.faveLoading!!.visibility = View.VISIBLE
        } else {
            binding?.faveLoading!!.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}