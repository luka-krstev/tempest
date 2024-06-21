package com.elfak.tempest.presentation.avatar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.elfak.tempest.presentation.shared.components.Button
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.presentation.shared.components.ErrorBox

@Composable
fun AvatarScreen(navController: NavController) {
    val avatarViewModel = viewModel<AvatarViewModel>()
    val state = avatarViewModel.state

    if (state.success) {
        navController.navigate(Screen.Home.route)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                avatarViewModel.pick(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = state.uri,
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Customize Your Avatar",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF5C5E63)
                ),
                text = "Personalize your profile with a unique avatar. This helps others recognize you easily."
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(state.error.isNotEmpty()) {
            ErrorBox(state.error)
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Column(
            modifier = Modifier
                .padding(top = 24.dp)
        ) {
            Button(
                text = "Choose your avatar",
                type = "ternary",
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                text = "Continue",
                loading = state.loading,
                disabled = !state.selected,
                onClick = {
                    avatarViewModel.onEvent(AvatarFormEvent.Submit)
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AvatarScreenPreview() {
    AvatarScreen(navController = rememberNavController())
}