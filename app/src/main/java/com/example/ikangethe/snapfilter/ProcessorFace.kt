package com.example.ikangethe.snapfilter

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.otaliastudios.cameraview.CameraView

class FaceProcessor(private val cameraView: CameraView, private val overlayView: OverlayView){
    private val options = FirebaseVisionFaceDetectorOptions.Builder()
        .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
        .build()

    private val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

    fun startProcessing(){
        cameraView.addFrameProcessor{frame ->

            if(frame.size != null){
                val rotation = frame.rotation/90
                if(rotation/2==0){
                    overlayView.previewWidth = cameraView.previewSize?.width
                    overlayView.previewHeight = cameraView.previewSize?.height
                } else {
                    overlayView.previewWidth = cameraView.previewSize?.height
                    overlayView.previewHeight = cameraView.previewSize?.width
                }

                val metadata = FirebaseVisionImageMetadata.Builder()
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                    .setWidth(frame.size.width)
                    .setHeight(frame.size.height)
                    .setRotation(rotation)
                    .build()

                val firebaseVisionImage = FirebaseVisionImage.fromByteArray(frame.data, metadata)

                detector.detectInImage(firebaseVisionImage).addOnSuccessListener { faceList ->
                    if(faceList.size > 0){
                        val face = faceList[0]

                        overlayView.face = face
                    }
                }
                }
            }
        }
    }