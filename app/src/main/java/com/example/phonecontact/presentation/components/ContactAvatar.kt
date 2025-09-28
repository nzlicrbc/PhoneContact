package com.example.phonecontact.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.phonecontact.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ContactAvatar(
    imageUrl: String?,
    name: String,
    size: Dp = Dimensions.profileImageSize,
    modifier: Modifier = Modifier,
    enableColorShadow: Boolean = true,
    cacheKey: String? = null
) {
    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color.Transparent) }
    val scope = rememberCoroutineScope()

    val finalImageUrl = remember(imageUrl, cacheKey) {
        when {
            imageUrl.isNullOrEmpty() -> null
            cacheKey != null -> "$imageUrl?cache=$cacheKey"
            else -> "$imageUrl?cache=${System.currentTimeMillis()}"
        }
    }

    LaunchedEffect(dominantColor) {
        println("Dominant color changed: $dominantColor")
    }

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (dominantColor != Color.Transparent && enableColorShadow) {
                    Modifier.drawBehind {
                        drawColorShadow(dominantColor)
                    }
                } else {
                    Modifier
                }
            )
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!finalImageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(finalImageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .listener(
                        onSuccess = { _, result ->
                            if (enableColorShadow) {
                                scope.launch(Dispatchers.IO) {
                                    extractDominantColor(result)?.let { color ->
                                        withContext(Dispatchers.Main) {
                                            dominantColor = Color(color)
                                        }
                                    }
                                }
                            }
                        },
                        onError = { _, _ ->
                            println("Image load failed for: $finalImageUrl")
                        }
                    )
                    .build(),
                contentDescription = "Profile Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background),
                contentAlignment = Alignment.Center
            ) {
                if (name.isNotEmpty()) {
                    Text(
                        text = name.first().uppercase(),
                        fontSize = (size.value / 2.5).sp,
                        color = TextSecondary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(size * 0.6f),
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}

private fun extractDominantColor(result: SuccessResult): Int? {
    return try {
        val drawable = result.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap

            val scaledBitmap = if (bitmap.width > 150 || bitmap.height > 150) {
                Bitmap.createScaledBitmap(bitmap, 150, 150, false)
            } else {
                bitmap
            }

            val palette = Palette.from(scaledBitmap)
                .maximumColorCount(8)
                .generate()

            palette.vibrantSwatch?.rgb
                ?: palette.lightVibrantSwatch?.rgb
                ?: palette.darkVibrantSwatch?.rgb
                ?: palette.dominantSwatch?.rgb
        } else {
            null
        }
    } catch (e: Exception) {
        println("Color extraction error: ${e.message}")
        null
    }
}

private fun DrawScope.drawColorShadow(color: Color) {
    drawCircle(
        color = color.copy(alpha = 0.2f),
        radius = size.minDimension / 2 + 8.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.1f),
        radius = size.minDimension / 2 + 4.dp.toPx()
    )
}