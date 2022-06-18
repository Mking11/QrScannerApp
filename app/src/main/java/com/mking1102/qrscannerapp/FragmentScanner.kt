package com.mking1102.qrscannerapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import com.mking1102.qrscannerapp.databinding.FragmentScannerBinding
import java.util.concurrent.Executors

class FragmentScanner : Fragment() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val permission = android.Manifest.permission.CAMERA
    private val request = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (!it) {
            requestPermissions()
        } else {
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            startCamera()
        }
    }

    private lateinit var binding: FragmentScannerBinding
    private lateinit var previewView: PreviewView
    private lateinit var imageAnalyzer: ImageAnalysis

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentScannerBinding.inflate(inflater, container, false)


        previewView = binding.previewView

        imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()

        // Inflate the layout for this fragment
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
        } else {

            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            startCamera()
        }

        binding.rescan.setOnClickListener {
            startCamera()
        }
        return binding.root
    }


    private fun startCamera() {

        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            preview.setSurfaceProvider(previewView.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA



            imageAnalyzer.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                QrCodeAnalyzer(object : ImageAnalyzerListener {
                    override fun onImageAnalyzed(rawValue: String?) {
                        Toast.makeText(
                            requireContext(),
                            rawValue ?: "Error Scanning",
                            Toast.LENGTH_LONG
                        ).show()
                        imageAnalyzer.clearAnalyzer()
                    }


                })
            )

            try {
                provider.unbindAll()
                provider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (e: Exception) {
                Log.e("Camera Error ", "", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestPermissions() {
        request.launch(permission)
    }

}