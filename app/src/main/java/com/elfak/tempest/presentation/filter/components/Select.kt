package com.elfak.tempest.presentation.filter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfak.tempest.R
import com.elfak.tempest.model.User
import com.elfak.tempest.noAnimationClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    items: List<User>,
    selected: User,
    onUserSelected: (User) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Text(
        style = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF5C5E63)
        ),
        text = "Author"
    )
    Spacer(modifier = Modifier.height(8.dp))
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE3E4E5),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .noAnimationClickable {
                    expanded = true
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(selected.fullName)
            AnimatedVisibility(
                visible = selected.fullName != "",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    tint = Color(0xFF5C5E63),
                    contentDescription = "Remove author",
                    modifier = Modifier.noAnimationClickable {
                        onUserSelected(User())
                    }
                )
            }
        }
    }

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            sheetState = sheetState,
            containerColor = Color.White,
        ) {
            Surface(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                LazyColumn {
                    items(items) { user ->
                        Row(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = user.fullName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noAnimationClickable {
                                        expanded = false
                                        onUserSelected(user)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}