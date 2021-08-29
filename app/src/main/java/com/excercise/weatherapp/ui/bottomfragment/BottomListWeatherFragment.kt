package com.excercise.weatherapp.ui.bottomfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.excercise.weatherapp.core.adapter.FavoriteCountryAdapter
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.databinding.FragmentBottomListWeatherBinding
import com.excercise.weatherapp.ui.WeatherViewModel
import com.excercise.weatherapp.utils.DataMapper.listFavoriteLocation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomListWeatherFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomListWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteCountryAdapter
    private val listCountry = ArrayList<FavoriteCountryByCoordinate>()
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomListWeatherBinding.inflate(inflater, container, false)
        listCountry.addAll(listFavoriteLocation)
        setupRvFavorite()
        observeFavoriteCountry()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun observeFavoriteCountry() = CoroutineScope(Dispatchers.Main).launch {
        viewModel.getFavoriteCountry().observe(this@BottomListWeatherFragment, { response ->
            if (response.isNullOrEmpty()) {
                favoriteAdapter.differ.submitList(listCountry.toMutableList())
            } else {
                listCountry.addAll(response)
                listCountry.reverse()
                favoriteAdapter.differ.submitList(listCountry.toMutableList())
            }
        })
    }

    private fun setupRvFavorite() {
        favoriteAdapter = FavoriteCountryAdapter()
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }

    companion object {
        const val TAG = "BottomListWeatherFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}