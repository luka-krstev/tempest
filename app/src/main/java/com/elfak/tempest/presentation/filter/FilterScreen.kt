package com.elfak.tempest.presentation.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elfak.tempest.R
import com.elfak.tempest.common.components.Input
import com.elfak.tempest.common.components.ToggleButtons
import com.elfak.tempest.presentation.filter.components.Select
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.presentation.filter.components.DateTimePicker
import com.elfak.tempest.toDate
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection

@Composable
fun FilterScreen(navController: NavController, filterViewModel: FilterViewModel) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(top = 24.dp)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(128.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE6E7EA),
                        shape = RoundedCornerShape(128.dp)
                    )
                    .padding(8.dp)
                    .noAnimationClickable {
                        navController.popBackStack()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Go back",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF75777C)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Filter tickets",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                ),
            )
        }
        Column(
            modifier = Modifier.padding(top = 32.dp)
        ) {
            ToggleButtons(
                options = listOf("Low", "Medium", "High"),
                selectedOption = filterViewModel.state.priority,
                allowEmpty = true,
                onSelectionChange = {
                    filterViewModel.onEvent(FilterEvent.PriorityChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Input(
                label = "Title",
                value = filterViewModel.state.title
            ) {
                filterViewModel.onEvent(FilterEvent.TitleChanged(it))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Input(
                label = "Content",
                value = filterViewModel.state.content
            ) {
                filterViewModel.onEvent(FilterEvent.ContentChanged(it))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Select(
                items = filterViewModel.authors,
                selected = filterViewModel.state.author,
                onUserSelected = {
                    filterViewModel.onEvent(FilterEvent.AuthorChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Input(
                label = "Radius (meters)",
                value = if (filterViewModel.state.radius == null) "" else filterViewModel.state.radius.toString()
            ) { value ->
                if (value.isEmpty()) {
                    filterViewModel.onEvent(FilterEvent.RadiusChanged(null))
                }
                if (value.matches(Regex("\\d+"))) {
                    filterViewModel.onEvent(FilterEvent.RadiusChanged(value.toInt()))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            DateTimePicker(selected = filterViewModel.state.created) {
                filterViewModel.onEvent(FilterEvent.CreatedChanged(it))
            }
        }
    }
}