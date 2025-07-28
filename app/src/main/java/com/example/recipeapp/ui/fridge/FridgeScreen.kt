package com.example.recipeapp.ui.fridge

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.mutableIntStateOf
data class Ingredient(
    val name: String,
    val count: MutableState<Int>,
    val expiry: String,
    val memo: String = ""
)

@Composable
fun FridgeScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    val ingredients = remember { mutableStateListOf<Ingredient>() }
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val sortedIngredients = remember(ingredients) {
        derivedStateOf {
            ingredients.sortedBy {
                sdf.parse(it.expiry)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(navController)

        Text(
            text = "내 냉장고 재료",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sortedIngredients.value) { ingredient ->
                val today = Calendar.getInstance().time
                val expiryDate = try {
                    sdf.parse(ingredient.expiry)
                } catch (_: Exception) {
                    null
                }

                val daysLeft = expiryDate?.let {
                    val diff = it.time - today.time
                    (diff / (1000 * 60 * 60 * 24)).toInt()
                } ?: Int.MAX_VALUE

                val expiryColor = when {
                    daysLeft <= 3 -> Color.Red
                    daysLeft <= 7 -> Color(0xFFFFA500)
                    else -> Color.Gray
                }

                var isEditingCount by remember { mutableStateOf(false) }
                var tempCountText by remember { mutableStateOf(ingredient.count.value.toString()) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = ingredient.name, style = MaterialTheme.typography.titleMedium)
                            if (ingredient.memo.isNotBlank()) {
                                Text(
                                    text = ingredient.memo,
                                    fontSize = 13.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        if (ingredient.count.value > 1) {
                                            ingredient.count.value--
                                            tempCountText = ingredient.count.value.toString()
                                        }
                                    }) {
                                        Icon(Icons.Default.Remove, contentDescription = "감소")
                                    }

                                    if (isEditingCount) {
                                        OutlinedTextField(
                                            value = tempCountText,
                                            onValueChange = {
                                                tempCountText = it.filter { c -> c.isDigit() }
                                            },
                                            modifier = Modifier.width(70.dp),
                                            textStyle = TextStyle(fontSize = 16.sp),
                                            singleLine = true
                                        )
                                        IconButton(onClick = {
                                            val newValue = tempCountText.toIntOrNull()
                                            if (newValue != null && newValue > 0) {
                                                ingredient.count.value = newValue
                                            }
                                            isEditingCount = false
                                        }) {
                                            Icon(Icons.Default.Check, contentDescription = "확인")
                                        }
                                    } else {
                                        Text(
                                            text = "${ingredient.count.value}개",
                                            fontSize = 16.sp,
                                            modifier = Modifier.clickable {
                                                isEditingCount = true
                                            }
                                        )
                                    }

                                    IconButton(onClick = {
                                        ingredient.count.value++
                                        tempCountText = ingredient.count.value.toString()
                                    }) {
                                        Icon(Icons.Default.Add, contentDescription = "증가")
                                    }
                                }

                                Text(
                                    text = "유통기한: ${ingredient.expiry} (D-${daysLeft})",
                                    fontSize = 13.sp,
                                    color = expiryColor
                                )
                            }
                        }

                        IconButton(
                            onClick = { ingredients.remove(ingredient) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제")
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { showDialog = true }) {
                Text("재료 추가")
            }
            Button(onClick = { navController.navigate("shopping") }) {
                Text("장보기 리스트")
            }
        }

        if (showDialog) {
            AddIngredientDialog(
                onDismiss = { showDialog = false },
                onAdd = { name, count, expiry, memo ->
                    ingredients.add(Ingredient(name, mutableIntStateOf(count), expiry, memo))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddIngredientDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Int, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var countText by remember { mutableStateOf("1") }
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    val context = LocalContext.current
    var showErrors by remember { mutableStateOf(false) }

    val count = countText.toIntOrNull() ?: 1
    val expiry = "${year.padStart(4, '0')}-${month.padStart(2, '0')}-${day.padStart(2, '0')}"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("재료 추가")
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "닫기")
                }
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; showErrors = false },
                    label = { Text("재료 이름") },
                    isError = showErrors && name.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = countText,
                    onValueChange = { countText = it.filter { c -> c.isDigit() } },
                    label = { Text("수량") },
                    modifier = Modifier.width(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("유통기한 (yyyy-MM-dd)", style = MaterialTheme.typography.labelMedium)
                Row {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it.filter { c -> c.isDigit() } },
                        label = { Text("년") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = month,
                        onValueChange = { month = it.filter { c -> c.isDigit() } },
                        label = { Text("월") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it.filter { c -> c.isDigit() } },
                        label = { Text("일") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    label = { Text("메모 (선택)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val isValidDate = try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(expiry)
                    true
                } catch (_: Exception) {
                    false
                }

                if (name.isBlank() || !isValidDate) {
                    showErrors = true
                    Toast.makeText(context, "입력값을 확인하세요", Toast.LENGTH_SHORT).show()
                } else {
                    onAdd(name.trim(), count, expiry, memo.trim())
                }
            }) {
                Text("추가")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
