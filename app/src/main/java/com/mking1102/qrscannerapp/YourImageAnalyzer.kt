package com.mking1102.qrscannerapp

import android.annotation.SuppressLint
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode


class YourImageAnalyzer : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC
    ).build()
    private val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image


        if (mediaImage != null) {

            val result: Task<MutableList<Barcode>> =
                scanner.process(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    .addOnSuccessListener {

                    }.addOnFailureListener {

                    }
        }
    }

}


