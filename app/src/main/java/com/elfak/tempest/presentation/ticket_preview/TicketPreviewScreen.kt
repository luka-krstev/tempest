package com.elfak.tempest.presentation.ticket_preview

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
import com.elfak.tempest.utility.navigation.Screen
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.common.components.Button
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TicketPreviewScreen(navController: NavController, id: String) {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val ticketPreviewViewModel = viewModel<TicketPreviewViewModel>()
    val ticket = ticketPreviewViewModel.ticket

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    text = "Ticket summary",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Left
                    ),
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Title",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = ticket.title,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Content",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = ticket.content,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Content",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = ticket.content,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Location",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = "${ticket.latitude} ${ticket.longitude}",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Created",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = dateFormat.format(ticket.timeCreated),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Priority",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = ticket.priority,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                text = "Solve",
            ) {
                ticketPreviewViewModel.solve(id) {
                    navController.popBackStack()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                text = "Edit",
                type = "ternary"
            ) {
                navController.navigate(Screen.ModifyTicket.createRoute(ticket.latitude, ticket.longitude, id))
            }
        }
    }
}