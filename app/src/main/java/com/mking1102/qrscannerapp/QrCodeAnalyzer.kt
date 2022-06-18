package com.mking1102.qrscannerapp

import android.annotation.SuppressLint
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode


class QrCodeAnalyzer(private val listener: ImageAnalyzerListener) : ImageAnalysis.Analyzer {
    private val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC
    ).build()


    private val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image
        if (mediaImage != null) {
            scanner.process(mediaImage, imageProxy.imageInfo.rotationDegrees)
                .addOnSuccessListener { barcode ->
                    barcode.forEach {
                        listener.onImageAnalyzed(it.rawValue)
                    }
                    mediaImage.close()
                    imageProxy.close()
                }.addOnFailureListener {
                    mediaImage.close()
                    imageProxy.close()
                }
        }

    }
}


