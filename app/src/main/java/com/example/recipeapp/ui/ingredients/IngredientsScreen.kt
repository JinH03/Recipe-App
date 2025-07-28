package com.example.recipeapp.ui.ingredients

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    var preferredInput by remember { mutableStateOf("") }
    var preferredIngredients by remember { mutableStateOf(setOf<String>()) }
    val filterScrollState = rememberScrollState()

    val suggestions = remember(input) {
        if (input.isBlank()) emptyList()
        else allIngredients.filter {
            it.contains(input, ignoreCase = true) && !ingredients.contains(it)
        }.take(5)
    }

    Column {
        TopBar(navController)

        Column(modifier = Modifier.padding(16.dp)) {
            // 자동완성 입력창
            ExposedDropdownMenuBox(
                expanded = suggestions.isNotEmpty(),
                onExpandedChange = { }
            ) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("재료를 입력하세요") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(4.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )

                ExposedDropdownMenu(
                    expanded = suggestions.isNotEmpty(),
                    onDismissRequest = { }
                ) {
                    suggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            text = { Text(suggestion) },
                            onClick = {
                                ingredients = ingredients + suggestion
                                input = ""
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val item = input.trim()
                if (item.isNotBlank() && !ingredients.contains(item)) {
                    ingredients = ingredients + item
                }
                input = ""
            }, modifier = Modifier.align(Alignment.End)) {
                Text("추가")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("재료 목록")
            ingredients.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp, max = 64.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
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

            Button(onClick = { showFilterDialog = true }) {
                Text("필터⚙️")
            }

            Spacer(modifier = Modifier.height(24.dp))

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
