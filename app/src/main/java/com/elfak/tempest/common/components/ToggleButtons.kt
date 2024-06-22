package com.elfak.tempest.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfak.tempest.noAnimationClickable

@Composable
fun ToggleButtons(
    options: List<String>,
    selectedOption: String,
    onSelectionChange: (String) -> Unit
) {
    val firstItemShape: CornerBasedShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 0.dp,
        bottomEnd = 0.dp, bottomStart = 12.dp
    )

    val lastItemShape: CornerBasedShape = RoundedCornerShape(
        topStart = 0.dp, topEnd = 12.dp,
        bottomEnd = 12.dp, bottomStart = 0.dp
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        options.forEachIndexed { index, text ->
            val shape = when (index) {
                0 -> firstItemShape
                options.size - 1 -> lastItemShape
                else -> RectangleShape
            }

            val textColor by animateColorAsState(
                if (text == selectedOption) Color(0xFF54D490) else Color(0xFF5C5E63), label = ""
            )

            Row(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape)
                        .noAnimationClickable {
                            onSelectionChange(text)
                        }
                        .background(Color(0xFFEEEFF1))
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        ),
                )
            }
        }
    }
}