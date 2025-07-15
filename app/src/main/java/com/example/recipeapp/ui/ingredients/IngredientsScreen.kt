package com.example.recipeapp.ui.ingredients

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@Composable
fun IngredientsScreen(
    onNext: (List<String>) -> Unit,
    navController: NavHostController
) {
    // 입력 재료
    var input by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }

    // 필터 다이얼로그 표시
    var showFilterDialog by remember { mutableStateOf(false) }

    // 필터 상태
    val allergyOptions = listOf("계란", "우유", "갑각류", "땅콩", "밀", "생선")
    var selectedAllergies by remember { mutableStateOf(setOf<String>()) }
    var vegetarianOnly by remember { mutableStateOf(false) }
    var preferredInput by remember { mutableStateOf("") }
    var preferredIngredients by remember { mutableStateOf(setOf<String>()) }

    // 가로 스크롤 상태
    val filterScrollState = rememberScrollState()

    Column {
        TopBar(navController)

        Column(modifier = Modifier.padding(16.dp)) {
            // 재료 입력창 + 추가 (중복 방지)
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
                    val item = input.trim()
                    if (item.isNotBlank() && !ingredients.contains(item)) {
                        ingredients = ingredients + item
                    }
                    input = ""
                }) {
                    Text("추가")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 입력된 재료 리스트 + 삭제 기능
            Text("재료 목록")
            ingredients.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp, max = 64.dp)  // 최소 40dp, 최대 64dp
                        .padding(4.dp)
                    ,
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)    // 박스 내부 여백
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(onClick = {
                            ingredients = ingredients.filterNot { it == item }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "삭제")
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            // 필터 설정
            Button(onClick = { showFilterDialog = true }) {
                Text("필터⚙️")
            }

            // 활성 필터 요약 (가로 스크롤)
            if (selectedAllergies.isNotEmpty() ||
                vegetarianOnly ||
                preferredIngredients.isNotEmpty()
            ) {
                Spacer(modifier = Modifier.height(4.dp))


                Row(
                    modifier = Modifier
                        .horizontalScroll(filterScrollState)
                        .padding(vertical = 8.dp)
                ) {
                    selectedAllergies.forEach { allergy ->
                        FilterChip(
                            selected = true,
                            onClick = { /* 팝업 열기 or 수정 */ },
                            label = { Text(allergy) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    if (vegetarianOnly) {
                        FilterChip(
                            selected = true,
                            onClick = { /* 수정 로직 */ },
                            label = { Text("채식만 보기") },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    preferredIngredients.forEach { pref ->
                        FilterChip(
                            selected = true,
                            onClick = { /* 수정 로직 */ },
                            label = { Text(pref) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
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

    // 필터 팝업
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("필터 설정") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("알러지 항목 선택")
                    allergyOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedAllergies.contains(option),
                                onCheckedChange = { checked ->
                                    selectedAllergies = if (checked)
                                        selectedAllergies + option
                                    else
                                        selectedAllergies - option
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(option)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("채식만 보기", modifier = Modifier.weight(1f))
                        Switch(
                            checked = vegetarianOnly,
                            onCheckedChange = { vegetarianOnly = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text("선호 재료 추가")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = preferredInput,
                            onValueChange = { preferredInput = it },
                            label = { Text("ex) 버섯") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val item = preferredInput.trim()
                            if (item.isNotBlank()) {
                                preferredIngredients = preferredIngredients + item
                            }
                            preferredInput = ""
                        }) {
                            Text("추가")
                        }
                    }

                    // 선호 재료 리스트 + 삭제 기능
                    preferredIngredients.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("- $item", modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                preferredIngredients = preferredIngredients - item
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "삭제")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("적용")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}