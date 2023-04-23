package com.example.whatsmyartworth

import  android.Manifest
//Constants used when saving the photo from cameraX (formatting)
object Constants {

    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS= 123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}