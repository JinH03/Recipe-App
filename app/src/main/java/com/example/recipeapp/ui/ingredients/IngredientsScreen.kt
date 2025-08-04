package com.example.recipeapp.ui.ingredients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.recipeapp.data.DefaultIngredientSamples
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsScreen(
    onNext: (List<String>) -> Unit,
    navController: NavHostController
) {
    val allIngredients = DefaultIngredientSamples

    var input by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }

    var showFilterDialog by remember { mutableStateOf(false) }
    val allergyOptions = listOf("계란", "우유", "갑각류", "땅콩", "밀", "생선")
    var selectedAllergies by remember { mutableStateOf(setOf<String>()) }
    var vegetarianOnly by remember { mutableStateOf(false) }

    val suggestions = remember(input) {
        if (input.isBlank()) emptyList()
        else allIngredients.filter {
            it.contains(input, ignoreCase = true) && !ingredients.contains(it)
        }.take(5)
    }

    Column {
        TopBar(navController)

        Column(modifier = Modifier.padding(16.dp)) {
            var expanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        value = input,
                        onValueChange = {
                            input = it
                            expanded = true
                        },
                        label = { Text("재료를 입력하세요") },
                        trailingIcon = {}, // ← 화살표 없애기
                        modifier = Modifier
                            .menuAnchor()
                            .weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                    )


                    ExposedDropdownMenu(
                        expanded = expanded && suggestions.isNotEmpty(),
                        onDismissRequest = { expanded = false }
                    ) {
                        suggestions.forEach { suggestion ->
                            DropdownMenuItem(
                                text = { Text(suggestion) },
                                onClick = {
                                    if (!ingredients.contains(suggestion)) {
                                        ingredients = ingredients + suggestion
                                    }
                                    input = ""
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val item = input.trim()
                        if (item.isNotBlank() && !ingredients.contains(item)) {
                            ingredients = ingredients + item
                        }
                        input = ""
                        expanded = false
                    },
                    modifier = Modifier.height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B5B9F))
                ) {
                    Text("추가")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("재료 목록")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                ingredients.forEach { item ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF1F3F4),
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(text = item, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = {
                                    ingredients = ingredients.filterNot { it == item }
                                },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "삭제",
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showFilterDialog = true }) {
                Text("필터⚙️")
            }

            if (selectedAllergies.isNotEmpty() || vegetarianOnly) {
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp
                ) {
                    selectedAllergies.forEach { allergy ->
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = { Text(allergy, color = Color.White) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFFF5500),
                                selectedContainerColor = Color(0xFFFF5500),
                                labelColor = Color.White,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    if (vegetarianOnly) {
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = { Text("채식만 보기", color = Color.White) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFFF5500),
                                selectedContainerColor = Color(0xFFFF5500),
                                labelColor = Color.White,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val allergyMap = mapOf(
                        "계란" to listOf("달걀", "메추리알", "계란"),
                        "우유" to listOf("우유", "치즈", "크림치즈", "모짜렐라치즈", "리코타치즈", "슬라이스치즈"),
                        "갑각류" to listOf("가리비", "대게", "새우", "홍합", "조개", "낙지", "멍게", "오징어", "해삼", "소라", "아귀", "대합", "미더덕", "우렁이"),
                        "땅콩" to listOf("땅콩", "피넛버터"),
                        "밀" to listOf("밀가루", "식빵", "스파게티면", "카스텔라"),
                        "생선" to listOf("고등어", "연어", "삼치", "명태", "청어", "도미", "참치", "흰살생선", "생선살")
                    )

                    val allergyFiltered = ingredients.filterNot { ingredient ->
                        selectedAllergies.any { allergy ->
                            allergyMap[allergy]?.contains(ingredient) == true
                        }
                    }

                    val meatList = listOf(
                        "가리비", "갈비", "고등어", "돼지 앞다리살", "낙지", "달걀", "닭가슴살", "닭날개", "닭똥집", "닭안심", "닭발", "닭봉", "닭죽",
                        "대게", "대합", "도미", "멍게", "메추리알", "모짜렐라치즈", "맛살", "명태", "비엔나소시지", "삼치", "새우", "소고기",
                        "소라", "스팸", "연어", "오리고기", "오징어", "우렁이", "우유", "우족", "육수", "위소시지", "장어", "참치", "치즈",
                        "치킨스톡", "크림치즈", "하몽", "한우", "홍합", "흰살생선", "생선살", "리코타치즈", "슬라이스치즈"
                    )

                    val vegetarianFiltered = if (vegetarianOnly) {
                        allergyFiltered.filterNot { meatList.contains(it) }
                    } else allergyFiltered

                    onNext(vegetarianFiltered)
                },
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
