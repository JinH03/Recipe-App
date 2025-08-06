// AddIngredientDialog.kt
package com.example.recipeapp.ui.fridge

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Int, String, String) -> Unit
) {
    val context = LocalContext.current

    // 자동완성용 재료 리스트 (예시)
    val allIngredients = listOf(
        "가리비", "가자미", "가지", "간장", "갈비", "감자", "강황", "건고추",
        "고구마", "고등어", "고추", "고추가루", "고추장", "곰취", "과일식초",
        "굴", "굴소스", "귀리", "귤", "그린빈", "기름", "깨", "꽁치", "꿀", "나쵸",
        "낙지", "다시마", "다슬기", "계란", "닭가슴살", "닭날개", "닭똥집", "닭안심",
        "닭발", "닭봉", "대게", "대파", "대합", "더덕", "도라지", "도미", "두부",
        "들기름", "딸기", "떡국떡", "땅콩", "라면사리", "레몬", "레몬즙", "로즈마리",
        "마늘", "마요네즈", "맛살", "멸치", "멜론", "명태", "모짜렐라치즈", "무",
        "무말랭이", "문어", "미나리", "미더덕", "밀가루", "바질", "방울토마토", "배",
        "배추", "백김치", "버섯", "버터", "베이컨", "보리", "부추", "브로콜리",
        "비엔나소시지", "사과", "살구", "삼치", "새우", "생강", "생선살", "생크림",
        "샐러리", "설탕", "소고기", "소라", "소면", "소시지", "소주", "송이버섯",
        "수박", "숙주", "순두부", "스팸", "스파게티면", "시금치", "시래기", "식빵",
        "식초", "쑥갓", "아귀", "아보카도", "아스파라거스", "애호박", "양배추",
        "양상추", "양송이버섯", "양파", "어묵", "연두부", "연어", "열무", "오리고기",
        "오렌지", "오이", "오징어", "옥수수", "우렁이", "우유", "우족", "우엉", "우삼겹",
        "우슬", "육수", "위소시지", "유자청", "율무", "은행", "인삼", "자두", "장어",
        "장조림", "잣", "전복", "전어", "정어리", "제육", "조개", "조청", "주꾸미",
        "참깨", "참기름", "참외", "참치", "청경채", "청양고추", "청어", "체다치즈",
        "체리", "초코", "치즈", "치킨스톡", "카레가루", "카스텔라", "케첩", "콩나물",
        "크림치즈", "토마토", "토마토소스", "파", "파인애플", "파프리카", "팥", "팽이버섯",
        "페퍼론치노", "포도", "표고버섯", "피망", "피자치즈", "피클", "하몽", "한우",
        "해삼", "햄", "호두", "호박", "홍고추", "홍합", "흰살생선", "흰후추"
    )

    var name by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("1") }
    var expiryYear by remember { mutableStateOf("") }
    var expiryMonth by remember { mutableStateOf("") }
    var expiryDay by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val filteredOptions = allIngredients.filter {
        it.contains(name, ignoreCase = true)
    }.take(5)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val expiry = "${expiryYear.padStart(4, '0')}-${expiryMonth.padStart(2, '0')}-${expiryDay.padStart(2, '0')}"
                val countInt = count.toIntOrNull() ?: 1
                onAdd(name, countInt, expiry, memo)
            }) {
                Text("추가")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("취소")
            }
        },
        title = { Text("재료 추가") },
        text = {
            Column {
                // ✅ 자동완성 DropDown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = name,
                        onValueChange = {
                            name = it
                            expanded = true
                        },
                        label = { Text("재료 이름") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor() // 필요 시 추가
                    )

                    ExposedDropdownMenu(
                        expanded = expanded && filteredOptions.isNotEmpty(),
                        onDismissRequest = { expanded = false }
                    ) {
                        filteredOptions.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection) },
                                onClick = {
                                    name = selection
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 수량
                OutlinedTextField(
                    value = count,
                    onValueChange = { count = it.filter { c -> c.isDigit() } },
                    label = { Text("수량") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("유통기한 (yyyy-MM-dd)")
                Row {
                    OutlinedTextField(
                        value = expiryYear,
                        onValueChange = { expiryYear = it.filter { c -> c.isDigit() } },
                        label = { Text("년") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = expiryMonth,
                        onValueChange = { expiryMonth = it.filter { c -> c.isDigit() } },
                        label = { Text("월") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedTextField(
                        value = expiryDay,
                        onValueChange = { expiryDay = it.filter { c -> c.isDigit() } },
                        label = { Text("일") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    label = { Text("메모 (선택)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
