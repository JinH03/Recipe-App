package com.example.recipeapp.ui.ingredients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun IngredientsScreen(
    onNext: (List<String>) -> Unit // "추천 요리 보기" 버튼 클릭 시 전달
) {
    var input by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }

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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("추천 요리 보기 🍳")
        }
    }
}
