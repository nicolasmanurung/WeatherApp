package com.excercise.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.excercise.weatherapp.core.adapter.WeeklyAdapter
import com.excercise.weatherapp.core.data.Resource
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.domain.model.MWeather
import com.excercise.weatherapp.databinding.FragmentFirstBinding
import com.excercise.weatherapp.utils.Constants
import com.excercise.weatherapp.utils.DataMapper.listFavoriteLocation
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class FirstFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var weeklyAdapter: WeeklyAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var selectedLat: Double? = null
    private var selectedLon: Double? = null
    private var countryName: String? = "Gdansk"

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        setupRvWeekly()

        // get cache
        observeCacheWeather()

        if (locationPermission()) {
            try {
                activity.let {
                    locationRequest = LocationRequest.create().apply {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    }
                    val builder = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                    val taskService = LocationServices.getSettingsClient(binding.root.context)
                        .checkLocationSettings(builder.build())

                    taskService.addOnSuccessListener { response ->
                        val states = response.locationSettingsStates
                        if (states?.isLocationPresent == true) {
                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(binding.root.context)
                            val lastLocation = fusedLocationProviderClient.lastLocation
                            lastLocation.apply {
                                addOnSuccessListener { location: Location? ->
                                    Log.d("LASTLOCATIONSUCCESS->", location.toString())
                                    if (location != null) {
                                        Log.d("CURRENTLOCATION->", location.latitude.toString())
                                        Log.d("CURRENTLOCATION->", location.longitude.toString())

                                        selectedLat = location.latitude
                                        selectedLon = location.longitude
                                        observeCurrentWeather()
                                    } else {
                                        selectedLat = listFavoriteLocation.first().lat
                                        selectedLon = listFavoriteLocation.first().lon
                                        observeCurrentWeather()
                                    }
                                }
                                addOnFailureListener {
                                    Log.d("LASTLOCATIONFAILED->", it.message.toString())
                                    Toast.makeText(
                                        binding.root.context,
                                        it.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                    taskService.addOnFailureListener { e ->
                        if (e is ResolvableApiException) {
                            try {
                                // Handle result in onActivityResult()
                                e.startResolutionForResult(
                                    it,
                                    MainActivity.LOCATION_SETTING_REQUEST
                                )
                            } catch (sendEx: IntentSender.SendIntentException) {
                                Log.d("BUG->", sendEx.message.toString())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestLocationPermission()
        }

        return binding.root
    }


    private fun observeCurrentWeather() =
        CoroutineScope(Dispatchers.Main).launch {
            selectedLat?.let {
                selectedLon?.let { it1 ->
                    viewModel.getLatestDetailData(countryName, it, it1)
                        .observe(viewLifecycleOwner, { response ->
                            when (response) {
                                is Resource.Success -> {
                                    Log.d("DATALIST->", response.data.toString())
                                    response.data?.let { populateCurrentData(it) }
                                }

                                is Resource.Error -> {

                                }

                                is Resource.Loading -> {

                                }
                            }
                        })
                }
            }
        }

    private fun observeCacheWeather() = CoroutineScope(Dispatchers.Main).launch {
        viewModel.getCacheDetailData(0).observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data?.isNotEmpty() == true) {
                        Log.d("DATACACHELIST->",response.data.toString())
                        populateCurrentData(response.data)
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })
    }

    private fun observeWeeklyWeather(idCountry: Int, lan: Double, lon: Double) =
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getLatestWeeklyData(idCountry, lan, lon).observe(
                viewLifecycleOwner, { response ->
                    when (response) {
                        is Resource.Success -> {
//                        Log.d("DATA ->", response.data.toString())
                            response.data?.let { weeklyAdapter.differ.submitList(it.listData.toMutableList()) }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            )
        }

    @SuppressLint("SetTextI18n")
    private fun populateCurrentData(currentWeather: List<MWeather>) {
        Glide.with(binding.root.context)
            .load(Constants.ICON_URL + currentWeather.first().iconUrl + ".png")
            .into(binding.imgLogoWeather)
        binding.txtNameWeather.text = currentWeather.first().weatherName
        binding.txtDescriptionWeather.text = currentWeather.first().weatherDescription
        binding.txtHumidity.text = "${currentWeather.first().humidity} %"
        binding.txtPressure.text = "${currentWeather.first().pressure} hPa"
        binding.txtTemp.text = "${currentWeather.first().temp} \u2103"

        observeWeeklyWeather(
            currentWeather.first().id,
            currentWeather.first().lat,
            currentWeather.first().lon
        )
    }

    private fun setupRvWeekly() {
        weeklyAdapter = WeeklyAdapter()
        binding.rvEstimatedWeather.apply {
            adapter = weeklyAdapter
            layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun locationPermission() = EasyPermissions.hasPermissions(
        binding.root.context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Fitur ini tidak dapat aktif tanpa mengizinkan Penggunaan Lokasi",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun refreshDataWithSelectedItem(item: FavoriteCountryByCoordinate) {
        observeCurrentWeather()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (locationPermission()) {

        } else {
            activity?.let { finishAffinity(it) }
            exitProcess(-1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}