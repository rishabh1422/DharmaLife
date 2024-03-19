package com.app.barcodescannerapp

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.app.barcodescannerapp.databinding.ActivityCaptureImageBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class CaptureImage : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var binding: ActivityCaptureImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityCaptureImageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
               2
            )
        } else {

        }

        binding.btnCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }
    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgViewer.setImageBitmap(imageBitmap)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            // Handle location
                            val latitude = location.latitude
                            val longitude = location.longitude
//                            val geocoder: Geocoder
//                            val addresses: List<Address>?
//                            geocoder = Geocoder(this, Locale.getDefault())
//
//                            addresses = geocoder.getFromLocation(latitude,
//                                longitude,
//                                1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//
//                            val address: String =
//                                addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//
//                            val city: String = addresses!![0].locality
//                            val state: String = addresses!![0].adminArea
//                            val country: String = addresses!![0].countryName
//                            val postalCode: String = addresses!![0].postalCode
//                            val knownName: String = addresses!![0].featureName
                            binding.address.setText(""+latitude+"\n"+longitude)

                        } else {
                            Toast.makeText(
                                this,
                                "Could not get location. Please make sure location services are enabled.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Error getting location: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
        }
    }

}