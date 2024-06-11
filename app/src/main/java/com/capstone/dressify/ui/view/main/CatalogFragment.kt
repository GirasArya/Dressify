package com.capstone.dressify.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.response.ClothingItemsItem
import com.capstone.dressify.databinding.FragmentCatalogBinding
import com.capstone.dressify.ui.adapter.CatalogAdapter
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel
import com.capstone.dressify.ui.viewmodel.MainViewModel
import com.capstone.dressify.factory.ViewModelFactory
import com.capstone.dressify.ui.adapter.LoadingStateAdapter
import kotlinx.coroutines.launch

class CatalogFragment : Fragment(), CatalogAdapter.OnFavoriteClickListener {
    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application, requireContext().applicationContext)
    }
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var favViewmodel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        val favViewmodel: FavoriteViewModel by viewModels {
            ViewModelFactory.getInstance(requireActivity().application, requireContext().applicationContext)
        }

        catalogAdapter = CatalogAdapter(emptyList(), favViewmodel, viewLifecycleOwner, this) // Initialize with empty list
        binding.rvCatalogGrid.adapter = catalogAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                catalogAdapter.retry()
            }
        )

        binding.rvCatalogGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        lifecycleScope.launch {
            mainViewModel.fetchProducts().observe(viewLifecycleOwner) { pagingData ->
                catalogAdapter.submitData(lifecycle, pagingData)
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        lifecycleScope.launch {
            mainViewModel.fetchProducts()
        }

        return binding.root
    }

    override fun onFavoriteClick(product: ClothingItemsItem) {
        favViewmodel.isItemFavorite(product.productDisplayName ?: "").observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                favViewmodel.deleteFavorite(product.productDisplayName ?: "", product.pictureLink ?: "")
            } else {
                favViewmodel.addFavorite(product.productDisplayName ?: "", product.pictureLink ?: "")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.catalogLoading.visibility = View.VISIBLE
        } else {
            binding.catalogLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
