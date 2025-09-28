package com.example.phonecontact.presentation.components

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
import coil.compose.rememberAsyncImagePainter
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
    enableColorShadow: Boolean = true
) {
    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color.Transparent) }
    val scope = rememberCoroutineScope()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        onSuccess = { state ->
            if (enableColorShadow) {
                scope.launch {
                    extractDominantColor(state.result)?.let { color ->
                        dominantColor = Color(color)
                    }
                }
            }
        }
    )

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
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
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

private suspend fun extractDominantColor(result: SuccessResult): Int? {
    return withContext(Dispatchers.IO) {
        try {
            val drawable = result.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val palette = Palette.from(bitmap).generate()

                palette.vibrantSwatch?.rgb
                    ?: palette.dominantSwatch?.rgb
                    ?: palette.mutedSwatch?.rgb
                    ?: palette.darkVibrantSwatch?.rgb
                    ?: palette.lightVibrantSwatch?.rgb
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

private fun DrawScope.drawColorShadow(color: Color) {
    drawCircle(
        color = color.copy(alpha = 0.3f),
        radius = size.minDimension / 2 + 12.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.2f),
        radius = size.minDimension / 2 + 8.dp.toPx()
    )

    drawCircle(
        color = color.copy(alpha = 0.1f),
        radius = size.minDimension / 2 + 4.dp.toPx()
    )
}