package com.rijaldev.history.ui.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.rijaldev.history.data.local.entity.StoryItemEntity
import com.rijaldev.history.databinding.LayoutInfoMapsBinding

class InfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    private val binding = LayoutInfoMapsBinding.inflate((context as Activity).layoutInflater)

    override fun getInfoContents(marker: Marker): View {
        val data = marker.tag as StoryItemEntity
        Glide.with(context.applicationContext)
            .asBitmap()
            .load(data.photoUrl)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean,
                ): Boolean = false

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (marker.isInfoWindowShown) {
                            marker.showInfoWindow()
                        }
                    }, 100)
                    return false
                }
            })
            .into(binding.ivStory)
        binding.tvTitle.text = marker.title

        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? = null
}