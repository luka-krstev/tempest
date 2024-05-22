package com.elfak.tempest.presentation.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Button(
    text: String,
    type: String = "primary",
    loading: Boolean = false,
    disabled: Boolean = false,
    onClick: () -> Unit
) {
    var backgroundStyling = Modifier.fillMaxWidth()
    var textStyling = TextStyle(
        color = Color.White,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    if (type == "primary") {
        backgroundStyling = backgroundStyling.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF54D490),
                    Color(0xFF0FC27B),
                )
            ),
            shape = RoundedCornerShape(10.dp)
        )
    } else if (type == "secondary") {
        textStyling = TextStyle(
            color = Color(0xFF75777C),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }

    if (disabled) {
        backgroundStyling = backgroundStyling.alpha(0.6f)
    }

    backgroundStyling = backgroundStyling
        .padding(12.dp)
        .clickable(onClick = {
            if (!loading && !disabled) {
                onClick()
            }
        })

    Box(
        modifier = backgroundStyling,
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.width(20.dp).height(20.dp),
                strokeWidth = 3.dp,
                color = Color.White,
                trackColor = Color(0xFFDEDEDE),
            )
        } else {
            Text(
                style = textStyling,
                text = text
            )
        }
    }
}