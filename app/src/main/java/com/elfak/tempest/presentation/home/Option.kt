package com.elfak.tempest.presentation.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elfak.tempest.noAnimationClickable

@Composable
fun Option(
    icon: Int,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(128.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE6E7EA),
                shape = RoundedCornerShape(128.dp)
            )
            .noAnimationClickable(onClick = onClick)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = description,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF75777C)
        )
    }
}