package com.lavanya.visitorcard

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.lang.Double.parseDouble

class ImageProcessor : ImageAnalysis.Analyzer {

    val TAG = javaClass.simpleName

    private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    override fun analyze(imageProxy: ImageProxy?, degrees: Int) {
        val mediaImage = imageProxy?.image
        val imageRotation = degreesToFirebaseRotation(degrees)
        if (mediaImage != null) {
            val image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
            // Pass image to an ML Kit Vision API

            val detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer

            detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    // Task completed successfully
                    val resultText = firebaseVisionText.text
                    for (block in firebaseVisionText.textBlocks) {
                        val blockText = block.text
//                        val blockConfidence = block.confidence
//                        val blockLanguages = block.recognizedLanguages
//                        val blockCornerPoints = block.cornerPoints
//                        val blockFrame = block.boundingBox
                        for (line in block.lines) {
                            val lineText = line.text
//                            val lineConfidence = line.confidence
//                            val lineLanguages = line.recognizedLanguages
//                            val lineCornerPoints = line.cornerPoints
//                            val lineFrame = line.boundingBox
                            for (element in line.elements) {
                                val elementText = element.text
//                                val elementConfidence = element.confidence
//                                val elementLanguages = element.recognizedLanguages
//                                val elementCornerPoints = element.cornerPoints
//                                val elementFrame = element.boundingBox

                                if(elementText.contains('-')){
                                  val split=  elementText.split('-')
                                    val phonenumber = "".toDouble()
                                   split.let {
                                       val part1 = it[0]
                                       val part2 = it[1]
                                   }

                                }
                                var numeric = true
                                var num: Double = "".toDouble()
                                try {
                                    num = parseDouble(elementText)
                                } catch (e: NumberFormatException) {
                                    numeric = false
                                }
                                if (numeric)
                                    Log.e(TAG, " Phone number $num")
                                else
                                    Log.e(TAG, " Phone number is not mentioned on card.")
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }

        }
    }
}