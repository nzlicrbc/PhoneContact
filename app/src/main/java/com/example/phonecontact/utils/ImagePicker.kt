package com.example.phonecontact.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File

class ImagePicker(
    private val context: Context,
    private val onImageSelected: (Uri?, ByteArray?) -> Unit
) {

    private fun createImageFile(): File {
        val imageFileName = "${Constants.IMAGE_FILE_PREFIX}${System.currentTimeMillis()}_"
        val storageDir = context.cacheDir
        return File.createTempFile(
            imageFileName,
            Constants.IMAGE_FILE_EXTENSION,
            storageDir
        )
    }

    fun createTempImageUri(): Uri {
        val imageFile = createImageFile()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}${Constants.FILE_PROVIDER_SUFFIX}",
            imageFile
        )
    }

    fun processImageUri(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                compressBitmap(bitmap)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun compressBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        var quality = Constants.INITIAL_COMPRESSION_QUALITY

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        while (outputStream.toByteArray().size > Constants.MAX_IMAGE_SIZE_BYTES &&
            quality > Constants.MIN_COMPRESSION_QUALITY) {
            outputStream.reset()
            quality -= Constants.COMPRESSION_STEP
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }

        return outputStream.toByteArray()
    }
}

@Composable
fun rememberImagePicker(
    onImageSelected: (Uri?, ByteArray?) -> Unit
): Pair<() -> Unit, () -> Unit> {
    val context = LocalContext.current
    val imagePicker = remember { ImagePicker(context, onImageSelected) }

    var tempCameraUri: Uri? = null

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            val imageBytes = imagePicker.processImageUri(tempCameraUri!!)
            onImageSelected(tempCameraUri, imageBytes)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val imageBytes = imagePicker.processImageUri(it)
            onImageSelected(it, imageBytes)
        }
    }

    val launchCamera = {
        tempCameraUri = imagePicker.createTempImageUri()
        cameraLauncher.launch(tempCameraUri)
    }

    val launchGallery = {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    return launchCamera to launchGallery
}