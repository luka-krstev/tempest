package com.elfak.tempest.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.R

@Composable
fun LoginScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(top = 24.dp)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF54D490),
                                Color(0xFF0FC27B),
                            )
                        ),
                    )
                    .padding(8.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Company Logo",
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    text = "Welcome back!"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF5C5E63)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    text = "Weave Your Fiber Network Fabric with Ease"
                )
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Column {
            Column{
                Text(
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF5C5E63)
                    ),
                    text = "Email"
                )
                Spacer(modifier = Modifier.height(8.dp))
                BasicTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE3E4E5),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column{
                Text(
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF5C5E63)
                    ),
                    text = "Password"
                )
                Spacer(modifier = Modifier.height(8.dp))
                BasicTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE3E4E5),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            style = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF54D490),
                            Color(0xFF0FC27B),
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(12.dp),
            text = "Login"
        )
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}