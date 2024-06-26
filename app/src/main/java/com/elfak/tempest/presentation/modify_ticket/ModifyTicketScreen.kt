package com.elfak.tempest.presentation.modify_ticket

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
import com.elfak.tempest.common.components.Button
import com.elfak.tempest.common.components.Input
import com.elfak.tempest.common.components.ToggleButtons

@Composable
fun ModifyTicketScreen(navController: NavController, latitude: Double, longitude: Double, id: String?) {
    val options = listOf("Low", "Medium", "High")
    val factory = ModifyTicketViewModelFactory(id, latitude, longitude)
    val modifyTicketViewModel = viewModel<ModifyTicketViewModel>(factory = factory)

    LaunchedEffect(modifyTicketViewModel.state.success) {
        if (modifyTicketViewModel.state.success) {
            navController.popBackStack()
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
                text = if (id == null) "Create a new ticket" else "Edit a ticket",
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
                selectedOption = modifyTicketViewModel.state.priority,
                allowEmpty = true,
                onSelectionChange = {
                    modifyTicketViewModel.onEvent(ModifyTicketFormEvent.PriorityChanged(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = modifyTicketViewModel.state.title, label = "Title") {
                modifyTicketViewModel.onEvent(ModifyTicketFormEvent.TitleChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = modifyTicketViewModel.state.content, label = "Content", maxLines = 4) {
                modifyTicketViewModel.onEvent(ModifyTicketFormEvent.ContentChanged(it))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            text = if (id == null) "Create" else "Edit",
            loading = modifyTicketViewModel.state.loading,
        ) {
            modifyTicketViewModel.onEvent(ModifyTicketFormEvent.Submit)
        }
    }
}