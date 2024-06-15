package com.elfak.tempest.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import android.graphics.Color as AndroidColor

@Composable
fun Dot(
    title: String,
    position: LatLng,
    color: Color,
    modifier: Modifier = Modifier
) {
    MarkerComposable(
        state = MarkerState(position = position)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(
                    color = color.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(128.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(128.dp)
                    )
            )
        }
    }
}