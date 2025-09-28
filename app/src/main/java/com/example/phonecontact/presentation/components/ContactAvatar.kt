package com.example.phonecontact.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
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
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.phonecontact.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
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
    var dominantColor by remember { mutableStateOf<Color?>(null) }
    val scope = rememberCoroutineScope()

    val finalImageUrl = remember(imageUrl, cacheKey) {
        when {
            imageUrl.isNullOrEmpty() -> null
            cacheKey != null -> "$imageUrl?cache=$cacheKey"
            else -> "$imageUrl?cache=${System.currentTimeMillis()}"
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (dominantColor != null && enableColorShadow) {
                    Modifier.drawBehind {
                        drawColorShadow(dominantColor!!)
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
                                    try {
                                        val extractedColor = extractDominantColor(result)

                                        extractedColor?.let { colorInt ->
                                            val color = Color(colorInt)

                                            withContext(Dispatchers.Main) {
                                                dominantColor = color
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        },
                        onError = { _, throwable ->
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

@RequiresApi(Build.VERSION_CODES.O)
private suspend fun extractDominantColor(result: SuccessResult): Int? {
    return withContext(Dispatchers.IO) {
        try {
            val drawable = result.drawable

            if (drawable is BitmapDrawable) {
                val originalBitmap = drawable.bitmap

                if (originalBitmap.isRecycled) {
                    return@withContext null
                }

                val workingBitmap = if (originalBitmap.config == Bitmap.Config.HARDWARE) {
                    originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
                } else {
                    originalBitmap
                }

                val dominantColor = extractDominantColorManually(workingBitmap)
                if (workingBitmap != originalBitmap) {
                    workingBitmap.recycle()
                }

                dominantColor
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

private fun extractDominantColorManually(bitmap: Bitmap): Int? {
    return try {
        val width = bitmap.width
        val height = bitmap.height

        val stepSize = maxOf(1, minOf(width, height) / 20)

        val colorMap = mutableMapOf<Int, Int>()
        var totalPixels = 0

        for (y in 0 until height step stepSize) {
            for (x in 0 until width step stepSize) {
                val pixel = bitmap.getPixel(x, y)

                val alpha = (pixel shr 24) and 0xFF
                if (alpha < 128) continue

                val red = (pixel shr 16) and 0xFF
                val green = (pixel shr 8) and 0xFF
                val blue = pixel and 0xFF

                val brightness = (red + green + blue) / 3
                if (brightness < 30 || brightness > 225) continue

                val groupedColor = groupSimilarColors(pixel)

                colorMap[groupedColor] = (colorMap[groupedColor] ?: 0) + 1
                totalPixels++
            }
        }

        println("Sampled $totalPixels pixels, found ${colorMap.size} unique color groups")

        if (colorMap.isEmpty()) return null

        val dominantColor = colorMap.maxByOrNull { it.value }?.key
        val dominantCount = colorMap[dominantColor] ?: 0
        val percentage = (dominantCount.toFloat() / totalPixels * 100).toInt()

        println("Dominant color appears in $percentage% of sampled pixels")

        if (percentage < 5) return null

        dominantColor
    } catch (e: Exception) {
        println("Manual color extraction error: ${e.message}")
        null
    }
}

private fun groupSimilarColors(color: Int): Int {
    val red = ((color shr 16) and 0xFF) / 32 * 32
    val green = ((color shr 8) and 0xFF) / 32 * 32
    val blue = (color and 0xFF) / 32 * 32

    return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
}

private fun DrawScope.drawColorShadow(color: Color) {
    val baseRadius = size.minDimension / 2

    drawCircle(
        color = color.copy(alpha = 0.06f),
        radius = baseRadius + 16.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.08f),
        radius = baseRadius + 14.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.10f),
        radius = baseRadius + 12.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.12f),
        radius = baseRadius + 10.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.14f),
        radius = baseRadius + 8.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.16f),
        radius = baseRadius + 6.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.18f),
        radius = baseRadius + 4.dp.toPx()
    )
}