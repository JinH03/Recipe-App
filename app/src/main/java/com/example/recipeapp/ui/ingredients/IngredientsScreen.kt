package com.example.recipeapp.ui.ingredients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar
import androidx.compose.foundation.shape.RoundedCornerShape // 이 줄을 꼭 추가!

@Composable
fun IngredientsScreen(
    onNext: (List<String>) -> Unit,
    navController: NavHostController
) {
    var input by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }

    Column {
        TopBar(navController)

        Column(modifier = Modifier.padding(16.dp)) {
            // 입력창 + 추가 버튼
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("재료를 입력하세요") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    if (input.isNotBlank()) {
                        ingredients = ingredients + input.trim()
                        input = ""
                    }
                }) {
                    Text("추가")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 입력된 재료 리스트
            Text("입력된 재료:")
            ingredients.forEach {
                Text("- $it")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 추천 버튼
            Button(
                onClick = { onNext(ingredients) },
                enabled = ingredients.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8F9CC6)),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("추천 요리 보기 🔍")
            }
        }
    }
}
