package com.example.hw1_tomerlevy.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hw1_tomerlevy.R
import com.example.hw1_tomerlevy.utilities.Player
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var selectedPlayer: Player? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d("DEBUGMAPREADY","DEBUG: Map is ready") //Log
        googleMap = map

        //show Israel map - default
        val israelLocation = LatLng(31.7767, 35.2345)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(israelLocation, 7f))

        //define settings for the map
        googleMap?.apply {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
        }

        selectedPlayer?.let { showPlayerLocation(it) }
    }

    //function for showing player's location
    fun showPlayerLocation(player: Player) {
        Log.d("DebugShowPlayerLocation","DEBUG: Showing location for player: $player") //Log
        selectedPlayer = player

        player.latitude?.let { //check if latitude exist
            lat -> player.longitude?.let { lng -> //check if longitude exist

                val location = LatLng(lat, lng)
                googleMap?.apply {
                    clear() //clear all the others marker
                    addMarker(
                        MarkerOptions()
                            .position(location)
                            .title("${player.name} - Score: ${player.score}") //create te title of the marker
                            .snippet("Game Mode: ${player.gameMode}")
                    )

                    //zoom in animation
                    animateCamera(
                        CameraUpdateFactory.newLatLngZoom(location, 15f),
                        2000,
                        null
                    )
                }
            }
        } ?: run {
            Log.d("DEBUGNOLOCATIONFORPLAYER","No location for player") //Log
            //if there is no location permission - back to israel
            val israelLocation = LatLng(31.7767, 35.2345)
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(israelLocation, 7f),
                1000,
                null
            )
        }
    }
}