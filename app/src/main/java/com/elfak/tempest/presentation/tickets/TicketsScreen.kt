package com.elfak.tempest.presentation.tickets

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elfak.tempest.R
import com.elfak.tempest.utility.navigation.Screen
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.presentation.filter.FilterViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TicketsScreen(
    navController: NavController,
    filterViewModel: FilterViewModel,
    location: Pair<Double, Double>?
) {
    val factory = remember { TicketsViewModelFactory(location) }
    val ticketsViewModel = viewModel<TicketsViewModel>(factory = factory)

    LaunchedEffect(filterViewModel.state) {
        ticketsViewModel.filter(filterViewModel.state)
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
                        text = "Tickets",
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
                        modifier = Modifier
                            .size(16.dp)
                            .noAnimationClickable {
                                navController.navigate(Screen.Filters.route)
                            },
                        tint = Color(0xFF75777C)
                    )
                }
            }
        }
        items(ticketsViewModel.tickets) { ticket ->
            val priority: Color = when (ticket.priority) {
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
                        navController.navigate(Screen.TicketPreview.createRoute(ticket.id))
                    }
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ticket.title,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ticket.priority,
                        fontWeight = FontWeight.Bold,
                        color = priority,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(
                                priority.copy(alpha = 0.2F),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(10.dp, 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ticket.content
                )
            }
        }
    }
}