package com.app.barcodescannerapp.LocationUtils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationServices
import java.io.IOException

object LocationUtils {



    private const val LOCATION_PERMISSION_REQUEST_CODE = 2
    fun startLocationUpdates(context: Context, onLocationReceived: (Location) -> Unit) {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, get the last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Use the location
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        // Do something with latitude and longitude
                        Log.d(
                            "Utility::",
                            "Got Location from Utility" + location.latitude.toString() + "," + location.longitude.toString()
                        )
                        onLocationReceived.invoke(it)
                    } else {
                        Log.d(
                            "Utility::",
                            "Got Location from Utility is nil" + location.latitude.toString() + "," + location.longitude.toString()
                        )
                        Toast.makeText(
                            context,
                            "Unable to get your current location. Please move your mobile 2 to 5 meters, and try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener { exception ->
                // Handle any errors
                Log.d("Utility::", exception.message.toString())
                Toast.makeText(
                    context,
                    exception.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun handleLocationPermissionResult(
        context: Context,
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted.invoke()
            } else {
                onPermissionDenied.invoke()
            }
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
}