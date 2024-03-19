package com.app.barcodescannerapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import com.app.barcodescannerapp.databinding.ActivityBarcodeScanBinding
import com.app.barcodescannerapp.databinding.ActivityMainBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class BarcodeScan : AppCompatActivity() {
    private lateinit var binding: ActivityBarcodeScanBinding
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var intentData=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBarcodeScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAction.setOnClickListener {
            finish()
        }

    }

    private fun iniBc() {
        barcodeDetector=BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource=CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(1920,1080)
            .setAutoFocusEnabled(true)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .build()

        binding.surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback{
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder) {
                try {
                       cameraSource.start(binding.surfaceView!!.holder)
                }catch (e : IOException)
                {

                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }

        })

        barcodeDetector.setProcessor(object :Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(applicationContext,"barcode scanner has been stopped",Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcode= detections.detectedItems
                if(barcode.size()!=0)
                {
                    binding.barcode!!.post {

                       intentData=barcode.valueAt(0).displayValue
                        binding.barcode.text="QR Code Information : "+intentData

                    }
                }

            }

        })

    }

    override fun onPause() {
        super.onPause()
         cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        iniBc()
    }
}