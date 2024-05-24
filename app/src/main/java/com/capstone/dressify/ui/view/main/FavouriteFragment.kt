package com.capstone.dressify.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.dressify.databinding.FragmentCatalogBinding
import com.capstone.dressify.ui.adapter.FavoriteAdapter
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel
import com.capstone.dressify.ui.viewmodel.ViewModelFactory

class FavouriteFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteAdapter

    private val favViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        favViewModel.getAllFavorite().observe(viewLifecycleOwner) { products ->
            if (products != null) {
                adapter.setListAdapter(products)
            }
        }

        adapter = FavoriteAdapter()
        binding?.rvCatalogGrid?.layoutManager = GridLayoutManager(requireContext(), 2)
        binding?.rvCatalogGrid?.setHasFixedSize(true)
        binding?.rvCatalogGrid?.adapter = adapter

        favViewModel.getAllFavorite().observe(viewLifecycleOwner) { products ->
            if (products != null) {
                adapter.setListAdapter(products)
                adapter.favoriteViewModel = favViewModel
            }
        }

        return binding?.root
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}