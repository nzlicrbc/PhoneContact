package com.example.phonecontact.ui.theme

import com.example.phonecontact.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Varela = FontFamily(
    Font(R.font.varela_round, FontWeight.Normal)
)
val Typography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = Varela,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = Varela,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = Varela,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        color = TextSecondary
    ),

    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = Varela,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        color = TextSecondary
    ),

    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = Varela,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = Varela,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        color = TextTertiary
    )
)