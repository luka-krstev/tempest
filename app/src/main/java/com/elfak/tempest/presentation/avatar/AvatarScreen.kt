package com.elfak.tempest.presentation.avatar

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.elfak.tempest.R
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.presentation.register.RegisterScreen
import com.elfak.tempest.presentation.shared.components.ErrorBox
import com.elfak.tempest.presentation.shared.preferences.AuthPreferences
import com.elfak.tempest.presentation.shared.preferences.AvatarPreferences
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel

@Composable
fun AvatarScreen(navController: NavController) {
    val authViewModel = viewModel<AuthViewModel>()
    val avatarState = authViewModel.avatar

    var selectedImageUri by remember { mutableStateOf<Uri?>(
        Uri.parse("android.resource://com.elfak.tempest/" + R.drawable.placeholder))
    }
    var selected by remember { mutableStateOf<Boolean>(false) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            selected = true
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
                model = selectedImageUri,
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
        AnimatedVisibility(avatarState is AuthViewModel.AvatarState.Error) {
            if (avatarState is AuthViewModel.AvatarState.Error) {
                ErrorBox(avatarState.message)
            }
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
                loading = avatarState == AuthViewModel.AvatarState.Loading,
                disabled = !selected,
                onClick = {
                    authViewModel.uploadAvatar(AuthPreferences.getUserID()!!, selectedImageUri!!)
                }
            )
        }
    }
    LaunchedEffect(avatarState) {
        when (avatarState) {
            is AuthViewModel.AvatarState.Success -> {
                AvatarPreferences.setExists(true);
                navController.navigate(Screen.Home.route)
            } else -> { }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AvatarScreenPreview() {
    AvatarScreen(navController = rememberNavController())
}