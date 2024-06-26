package com.elfak.tempest.presentation.filter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfak.tempest.R
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.toDate
import com.elfak.tempest.toLocalDate
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Composable
fun DateTimePicker(
    selected: Date?,
    onSelection: (date: Date?) -> Unit
) {
    val state = rememberUseCaseState(visible = false)
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    var text = ""
    if (selected != null) {
        text = dateFormat.format(selected)
    }

    DateTimeDialog(
        state = state,
        selection = DateTimeSelection.Date(
            selectedDate = selected?.toLocalDate() ?: LocalDate.now()
        ) { date ->
            onSelection(date.toDate())
        },
    )
    Text(
        style = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF5C5E63)
        ),
        text = "Created"
    )
    Spacer(modifier = Modifier.height(8.dp))
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE3E4E5),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .noAnimationClickable {
                    state.show()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text)
            AnimatedVisibility(
                visible = selected != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    tint = Color(0xFF5C5E63),
                    contentDescription = "Remove date",
                    modifier = Modifier.noAnimationClickable {
                        onSelection(null)
                    }
                )
            }
        }
    }

}