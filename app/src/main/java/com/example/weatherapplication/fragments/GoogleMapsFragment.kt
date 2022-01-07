package com.example.weatherapplication.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapplication.R
import com.example.weatherapplication.data.CityInfo
import com.example.weatherapplication.data.DailyForecast.City

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        //Each time the city changes, it will be shown on the map
        val Coord = LatLng(CityInfo.lat, CityInfo.lon)
        googleMap.addMarker(MarkerOptions()
            .position(Coord)
            .title("Marker in ${CityInfo.cityname}")
            .snippet("Lat: ${CityInfo.lat}    Lon: ${CityInfo.lon}")
            .draggable(true)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Coord))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.GoogleMapsFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}