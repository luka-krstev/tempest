package com.elfak.tempest.presentation.user_preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elfak.tempest.R
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.noAnimationClickable
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel
import com.elfak.tempest.presentation.shared.view_models.Report
import com.elfak.tempest.presentation.shared.view_models.ReportViewModel
import com.elfak.tempest.presentation.shared.view_models.User

@Composable
fun UserPreviewScreen(navController: NavController, uid: String) {
    val authViewModel = viewModel<AuthViewModel>()
    var user by remember { mutableStateOf<User>(User()) }

    LaunchedEffect(Unit) {
        authViewModel.getUserById(uid) {
            if (it != null) {
                user = it
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
                text = "Users",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                ),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp).fillMaxWidth()
        ) {
            AsyncImage(
                model = user.avatar,
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(96.dp)
                    .height(96.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.username,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE6E7EA),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Email",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = user.email,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Phone",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = user.phone,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Points",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF75777C)
            )
            Text(
                text = user.points.toString(),
                fontSize = 16.sp
            )
        }
    }
}