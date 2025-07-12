package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.recipeapp.ui.ingredients.IngredientsScreen
import com.example.recipeapp.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                // 전체 화면 배경 설정
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 재료 입력 화면 호출
                    IngredientsScreen(
                        onNext = { ingredientsList ->println("입력된 재료들: $ingredientsList")
                        }
                    )

                }
            }
        }
    }
}
