package com.example.phonecontact.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonecontact.ui.theme.*

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = TextPrimary
            ),
            cursorBrush = SolidColor(Blue),
            decorationBox = { innerTextField ->
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = TextTertiary
                                )
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (isError) DeleteRed else DividerColor,
                        thickness = 1.dp
                    )
                }
            }
        )
    }
}