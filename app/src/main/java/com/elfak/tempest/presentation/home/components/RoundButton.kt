package com.elfak.tempest.presentation.home.components

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
import com.elfak.tempest.R
import com.elfak.tempest.noAnimationClickable

@Composable
fun RoundedButton(
    active: Boolean = false,
    onClick: () -> Unit
) {
    var backgroundStyling: Modifier = Modifier
        .background(Color.White, shape = RoundedCornerShape(128.dp))
        .border(
            width = 1.dp,
            color = Color(0xFFE6E7EA),
            shape = RoundedCornerShape(128.dp)
        )

    backgroundStyling = backgroundStyling
        .noAnimationClickable(onClick = onClick)
        .padding(12.dp)

    Box(
        modifier = backgroundStyling,
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = active, label = "service") { it ->
            if (it) {
                Icon(
                    painter = painterResource(id = R.drawable.stop),
                    contentDescription = "Stop location service",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF75777C)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Start location service",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF54D490)
                )
            }
        }
    }
}