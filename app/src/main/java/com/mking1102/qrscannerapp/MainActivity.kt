package com.mking1102.qrscannerapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.mking1102.qrscannerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private val permission = android.Manifest.permission.CAMERA

    private val request = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (!it) {
            requestPermissions()
        }
    }

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
        }else{
            cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        }


      setContentView(binding.root)
    }

    private fun requestPermissions() {
        request.launch(permission)
    }


}