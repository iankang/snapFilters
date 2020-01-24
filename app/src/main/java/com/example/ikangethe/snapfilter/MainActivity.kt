package com.example.ikangethe.snapfilter

import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.WindowManager
import com.example.ikangethe.snapfilter.databinding.ActivityMainBinding
import com.otaliastudios.cameraview.CameraView

class MainActivity : AppCompatActivity() {

    private val PERMISION_REQUEST_CODE = 3

    lateinit var binding: ActivityMainBinding

    lateinit var  cameraview: CameraView
    lateinit var overlayView: OverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        checkCameraPermission()
    }

    private fun checkCameraPermission(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), PERMISION_REQUEST_CODE)

        }else{
            startFaceProcessor()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode == PERMISION_REQUEST_CODE){
            if(android.Manifest.permission.CAMERA == permissions[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startFaceProcessor()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startFaceProcessor(){
        lifecycle.addObserver(CameraLifeCycleObserver(binding.cameraView))

        val faceProcessor = FaceProcessor(binding.cameraView, binding.overlayView)
        faceProcessor.startProcessing()
    }
}
