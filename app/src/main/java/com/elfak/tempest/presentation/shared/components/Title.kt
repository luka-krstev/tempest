package com.elfak.tempest.presentation.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfak.tempest.R

@Composable
fun Title(title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF54D490),
                            Color(0xFF0FC27B),
                        )
                    ),
                )
                .padding(8.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Company Logo",
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Text(
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),
                text = title
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF5C5E63)
                ),
                modifier = Modifier.fillMaxWidth(),
                text = description
            )
        }
    }
}