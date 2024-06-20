package com.elfak.tempest.presentation.all_reports

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.presentation.report.validate
import com.elfak.tempest.presentation.shared.components.Button
import com.elfak.tempest.presentation.shared.components.Input
import com.elfak.tempest.presentation.shared.components.ToggleButtons
import com.elfak.tempest.presentation.shared.view_models.Report
import com.elfak.tempest.presentation.shared.view_models.ReportViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllReportsScreen(navController: NavController) {
    val reportViewModel = viewModel<ReportViewModel>()
    var allReports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        reportViewModel.fetchAllReports()
            .catch { exception -> exception.printStackTrace() }
            .onEach { reports ->
                allReports = reports
            }
            .launchIn(coroutineScope)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 24.dp)
    ) {
        stickyHeader {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9F9))
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
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
                        text = "Reports",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(128.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE6E7EA),
                            shape = RoundedCornerShape(128.dp)
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "Filter",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF75777C)
                    )
                }
            }
        }
        items(allReports) { report ->
            val priority: Color = when (report.priority) {
                "Low" -> Color(0xFF75777C)
                "Medium" -> Color(0xFFEDD308)
                "High" -> Color(0xFFFF5B59)
                else -> Color(0xFF75777C)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE6E7EA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .noAnimationClickable {
                        navController.navigate(Screen.ReportPreview.createRoute(report.id))
                    }
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = report.title,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = report.priority,
                        fontWeight = FontWeight.Bold,
                        color = priority,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(priority.copy(alpha = 0.2F), shape = RoundedCornerShape(12.dp))
                            .padding(10.dp, 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = report.content
                )
            }
        }
    }
}