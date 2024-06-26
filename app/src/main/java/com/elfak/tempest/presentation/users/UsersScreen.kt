package com.elfak.tempest.presentation.users

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.elfak.tempest.utility.navigation.Screen
import com.elfak.tempest.noAnimationClickable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UsersScreen(navController: NavController) {
    val usersViewModel = viewModel<UsersViewModel>()
    val color = Color(0xFF54D490)

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
                        text = "Users",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        ),
                    )
                }
            }
        }
        items(usersViewModel.users) { user ->
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
                        navController.navigate(Screen.UserPreview.createRoute(user.id))
                    }
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                    ) {
                        AsyncImage(
                            model = user.avatar,
                            contentDescription = "Users image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = user.fullName,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user.username,
                            )
                        }
                    }
                    Text(
                        text = user.points.toString(),
                        fontWeight = FontWeight.Bold,
                        color = color,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(color.copy(alpha = 0.2F), shape = RoundedCornerShape(12.dp))
                            .padding(10.dp, 4.dp)
                    )
                }
            }
        }
    }
}
