package com.app.barcodescannerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.app.barcodescannerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var requestCamera: ActivityResultLauncher<String>?=null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCamera=registerForActivityResult(ActivityResultContracts
            .RequestPermission(),)
        {
            if(it)
            {
              val intent =Intent(this,BarcodeScan::class.java)
                startActivity(intent)
            } else
            {
             Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT).show()
            }
        }
        binding.campureImage.setOnClickListener {
            val intent =Intent(this,CaptureImage::class.java)
            startActivity(intent)
        }

        binding.scan.setOnClickListener {
            requestCamera?.launch(android.Manifest.permission.CAMERA)
        }
    }
}