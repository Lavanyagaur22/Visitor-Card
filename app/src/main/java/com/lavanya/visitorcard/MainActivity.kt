package com.lavanya.visitorcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.PreviewConfig
import androidx.camera.core.*
import android.util.Size
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val executor = Executors.newSingleThreadExecutor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewFinder.post {
            startCamera()
        }
    }
    private fun startCamera() {

        //Specify the configuration for the preview
        val previewConfig = PreviewConfig.Builder()
            .apply {
                //Set the resolution of the captured image
                setTargetResolution(Size(1920, 1080))
            }
            .build()

        //Generate a preview
        val preview = Preview(previewConfig)

        //Add a listener to update preview automatically
        preview.setOnPreviewOutputUpdateListener {

            val parent = viewFinder.parent as ViewGroup

            //Remove thr old preview
            parent.removeView(viewFinder)

            //Add the new preview
            parent.addView(viewFinder, 0)
            viewFinder.surfaceTexture = it.surfaceTexture
        }

        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            // In our analysis, we care more about the latest image than
            // analyzing *every* image
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
        }.build()

        val imageAnalysis = ImageAnalysis(analyzerConfig).apply {
            setAnalyzer(executor, ImageProcessor())
        }

        /*  Bind use cases to lifecycle. If Android Studio complains about "this"
            being not a LifecycleOwner, try rebuilding the project or updating the appcompat dependency to
            version 1.1.0 or higher.
         */
        CameraX.bindToLifecycle(this, preview, imageAnalysis)
    }
}
