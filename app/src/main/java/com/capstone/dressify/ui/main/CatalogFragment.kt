package com.capstone.dressify.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.dressify.adapter.CatalogAdapter
import com.capstone.dressify.databinding.FragmentCatalogBinding
import com.capstone.dressify.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Observe the product list
        mainViewModel.productList.observe(viewLifecycleOwner) { products ->
            if (products != null) {
                val adapter = CatalogAdapter(products)
                binding.rvCatalogGrid.adapter = adapter
                binding.rvCatalogGrid.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        // Fetch products when the view is created
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.fetchProducts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
