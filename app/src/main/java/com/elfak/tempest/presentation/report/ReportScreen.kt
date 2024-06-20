package com.elfak.tempest.presentation.report

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elfak.tempest.R
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.presentation.shared.components.Button
import com.elfak.tempest.presentation.shared.components.Input
import com.elfak.tempest.presentation.shared.components.ToggleButtons
import com.elfak.tempest.presentation.shared.view_models.Report
import com.elfak.tempest.presentation.shared.view_models.ReportViewModel

fun validate(title: String, content: String): Boolean {
    if (title.isEmpty()) return false
    if (content.isEmpty()) return false
    return true
}

@Composable
fun ReportScreen(navController: NavController, latitude: Float, longitude: Float, uid: String?) {
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    val options = listOf("Low", "Medium", "High")
    var selectedOption by rememberSaveable { mutableStateOf(options.first()) }
    val valid by remember {
        derivedStateOf { validate(title, content) }
    }

    val reportViewModel = viewModel<ReportViewModel>()
    val reportState = reportViewModel.state


    LaunchedEffect(Unit) {
        if (uid != null) {
            reportViewModel.fetchReportByUid(uid) {
                if (it != null) {
                    title = it.title
                    content = it.content
                    selectedOption = it.priority
                }
            }
        }
    }

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
                text = if (uid == null) "Create a new report" else "Edit a report",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                ),
            )
        }
        Column(
            modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp)
        ) {
            ToggleButtons(
                options = options,
                selectedOption = selectedOption
            ) { selectedOption = it }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = title, label = "Title") { title = it }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = content, label = "Content", maxLines = 4) { content = it }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            text = if (uid == null) "Create" else "Edit",
            disabled = !valid,
            loading = reportState == ReportViewModel.ReportState.Loading,
        ) {
            if (uid == null) {
                reportViewModel.createReport(title, content, selectedOption, latitude, longitude)
            } else {
                reportViewModel.updateReport(uid, title, content, selectedOption)
            }
        }
    }

    LaunchedEffect(reportState) {
        when (reportState) {
            is ReportViewModel.ReportState.Success -> {
                navController.popBackStack()
            } else -> { }
        }
    }
}