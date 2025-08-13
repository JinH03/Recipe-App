package com.example.recipeapp.ui.fridge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.recipeapp.ui.components.TopBar
import com.example.recipeapp.model.Ingredient
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FridgeScreen(
    navController: NavHostController,
    viewModel: FridgeViewModel
) {
    val ingredients by viewModel.ingredients.collectAsState()

    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val today = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    val sorted = remember(ingredients) {
        ingredients.sortedBy {
            runCatching { sdf.parse(it.expiry)!!.time }.getOrElse { Long.MAX_VALUE }
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "재료 추가")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {

            if (sorted.isEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    Text("냉장고에 재료가 없습니다.", modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(sorted) { ingredient: Ingredient ->
                        FridgeItemCard(
                            ingredient = ingredient,
                            onDelete = { viewModel.removeIngredient(ingredient) },            // ✔ 이름 맞춤
                            onIncrease = { viewModel.increaseCount(ingredient) },             // ✔
                            onDecrease = { viewModel.decreaseCount(ingredient) },             // ✔
                            onEditCount = { new ->
                                val v = new.filter { it.isDigit() }
                                    .toIntOrNull()
                                    ?.coerceAtLeast(1)
                                    ?: ingredient.count.value
                                viewModel.updateIngredientCount(ingredient, v)                // ✔
                            },
                            dDayColor = {
                                val d = runCatching {
                                    val exp = Calendar.getInstance().apply {
                                        time = sdf.parse(ingredient.expiry)!!
                                        set(Calendar.HOUR_OF_DAY, 0)
                                        set(Calendar.MINUTE, 0)
                                        set(Calendar.SECOND, 0)
                                        set(Calendar.MILLISECOND, 0)
                                    }
                                    ((exp.timeInMillis - today.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
                                }.getOrElse { Int.MAX_VALUE }
                                when {
                                    d <= 3 -> Color(0xFFEF5350) // 빨강
                                    d <= 7 -> Color(0xFFFFA726) // 주황
                                    else -> Color(0xFF9E9E9E) // 회색
                                }
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("재료 추가")
                }
                Button(
                    onClick = { navController.navigate("shopping") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("장보기 리스트")
                }
            }
        }
    }

    if (showDialog) {
        AddIngredientDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, count, expiry, memo ->
                viewModel.addIngredient(name, count, expiry, memo)
            }
        )
    }
}

/** 이름 끝의 [단위] 태그에서 단위를 추출. 없으면 '개' */
private fun parseUnitFromName(name: String): String {
    val m = Regex("""\[(.+?)]""").find(name)
    return m?.groupValues?.get(1) ?: "개"
}

@Composable
private fun FridgeItemCard(
    ingredient: Ingredient,
    onDelete: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onEditCount: (String) -> Unit,
    dDayColor: () -> Color
) {
    val editing = remember { mutableStateOf(false) }
    val tempText = remember { mutableStateOf(ingredient.count.value.toString()) }
    val unit = remember(ingredient.name) { parseUnitFromName(ingredient.name) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "삭제")
                }
            }

            Spacer(Modifier.height(8.dp))

            // 수량 영역 (증감/직접입력 포함) — 기존 로직 유지, 표기만 단위 추가
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("수량: ${ingredient.count.value}$unit")

                if (editing.value) {
                    OutlinedTextField(
                        value = tempText.value,
                        onValueChange = { tempText.value = it.filter { c -> c.isDigit() } },
                        modifier = Modifier.width(100.dp),
                        singleLine = true,
                        label = { Text("수정") }
                    )
                    TextButton(
                        onClick = {
                            onEditCount(tempText.value)
                            tempText.value = ingredient.count.value.toString()
                            editing.value = false
                        }
                    ) { Text("확인") }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        OutlinedButton(onClick = onDecrease) { Text("-") }
                        OutlinedButton(onClick = onIncrease) { Text("+") }
                        TextButton(onClick = { editing.value = true }) { Text("직접입력") }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // 유통기한 & 메모
            Text("유통기한: ${ingredient.expiry}", color = dDayColor())
            if (ingredient.memo.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text("메모: ${ingredient.memo}")
            }
        }
    }
}
