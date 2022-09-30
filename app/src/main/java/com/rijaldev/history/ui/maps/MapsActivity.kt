package com.rijaldev.history.ui.maps

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.rijaldev.history.R
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.databinding.ActivityMapsBinding
import com.rijaldev.history.ui.adapters.InfoWindowAdapter
import com.rijaldev.history.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundBuilder = LatLngBounds.Builder()
    private val mapsViewModel by viewModels<MapsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        mapsViewModel.getToken().observe(this) { token ->
            token?.let {
                mapsViewModel.getStoryWithLocation(it).observe(this, mapsObserver)
            }
        }
    }

    private val mapsObserver = Observer<Result<List<StoryItemEntity>>> { result ->
        when (result) {
            is Result.Success -> result.data?.let {
                addMarkers(it)
                val bounds = boundBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            } ?: run {
                Toast.makeText(this, getString(R.string.no_story_location), Toast.LENGTH_SHORT).show()
            }
            is Result.Error -> Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMarkers(data: List<StoryItemEntity>) {
        data.forEach {
            if (it.lat != null && it.lon != null) {
                val latLng = LatLng(it.lat, it.lon)
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.name)
                        .snippet(it.description)
                )
                boundBuilder.include(latLng)
                marker?.tag = it
                mMap.setInfoWindowAdapter(InfoWindowAdapter(this))
                mMap.setOnInfoWindowClickListener {
                    val intent = Intent(this, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.DETAIL, it.tag as StoryItemEntity)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }
}