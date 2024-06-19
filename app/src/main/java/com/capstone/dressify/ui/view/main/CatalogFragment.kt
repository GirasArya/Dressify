package com.capstone.dressify.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        val favViewmodel: FavoriteViewModel by viewModels {
            ViewModelFactory.getInstance(requireActivity().application, requireContext().applicationContext)
        }

        catalogAdapter = CatalogAdapter(emptyList(), favViewmodel, viewLifecycleOwner, this)
        val headerAdapter = LoadingStateAdapter { catalogAdapter.retry() }
        val footerAdapter = LoadingStateAdapter { catalogAdapter.retry() }
        val concatAdapter = catalogAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )

        binding.rvCatalogGrid.adapter = concatAdapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCatalogGrid.layoutManager = layoutManager

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && headerAdapter.itemCount > 0) {
                    2
                } else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.fetchProducts().observe(viewLifecycleOwner) { pagingData ->
                catalogAdapter.submitData(lifecycle, pagingData)
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
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
