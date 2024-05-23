package com.capstone.dressify.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.dressify.databinding.FragmentCatalogBinding
import com.capstone.dressify.ui.adapter.CatalogAdapter
import com.capstone.dressify.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            mainViewModel.fetchProducts()
        }

        mainViewModel.productList.observe(viewLifecycleOwner) { products ->
            if (products != null) {
                val adapter = CatalogAdapter(products)
                binding.rvCatalogGrid.adapter = adapter
                binding.rvCatalogGrid.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        return binding.root

    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading){
            binding.catalogLoading.visibility = View.VISIBLE
        }else{
            binding.catalogLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
