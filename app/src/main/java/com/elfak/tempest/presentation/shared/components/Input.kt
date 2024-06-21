package com.elfak.tempest.presentation.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Input(
    label: String = "label",
    type: String = "text",
    value: String,
    error: String = "",
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    var transformation = VisualTransformation.None;

    if (type == "password") {
        transformation = PasswordVisualTransformation()
    }

    Column{
        Text(
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF5C5E63)
            ),
            text = label
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            maxLines = maxLines,
            visualTransformation = transformation,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE3E4E5),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        AnimatedVisibility(error.isNotEmpty()) {
            Text(text = error, color = Color(0xFFF04438))
        }
    }
}