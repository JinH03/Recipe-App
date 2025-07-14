package com.example.recipeapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.navigation.Screen

@Composable
fun TopBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                // 즐겨찾기 페이지가 있다면 navController.navigate(...) 추가
            }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "즐겨찾기"
                )
            }

            IconButton(onClick = {
                navController.navigate(Screen.Ingredients.route)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "재료 입력"
                )
            }

            IconButton(onClick = {
                navController.navigate(Screen.Home.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "홈"
                )
            }

            IconButton(onClick = {
                // 잠금 관련 기능이 있다면 여기에 연결
            }) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "잠금"
                )
            }

            IconButton(onClick = {
                // 장바구니 페이지가 있다면 navController.navigate(...) 추가
            }) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "장바구니"
                )
            }
        }
    }
}
