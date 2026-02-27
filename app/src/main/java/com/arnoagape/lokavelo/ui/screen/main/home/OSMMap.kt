package com.arnoagape.lokavelo.ui.screen.main.home

import androidx.preference.PreferenceManager
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.arnoagape.lokavelo.domain.model.Bike
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OSMMap(
    userLocation: GeoPoint?,
    bikes: List<Bike>
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->

            Configuration.getInstance().userAgentValue = context.packageName
            Configuration.getInstance().load(
                context,
                PreferenceManager.getDefaultSharedPreferences(context)
            )

            MapView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(14.0)
                controller.setCenter(
                    userLocation ?: GeoPoint(43.2965, 5.3698)
                )
            }
        },
        update = { mapView ->

            mapView.overlays.clear()

            // 📍 Position utilisateur
            userLocation?.let {
                val marker = Marker(mapView)
                marker.position = it
                marker.title = "Vous êtes ici"
                mapView.overlays.add(marker)
            }

            // 🚲 Vélos
            bikes.forEach { bike ->
                val marker = Marker(mapView)
                marker.position = GeoPoint(
                    bike.location.latitude,
                    bike.location.longitude
                )
                marker.title = bike.title
                mapView.overlays.add(marker)
            }

            mapView.invalidate()
        }
    )
}