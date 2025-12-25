package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BingSearchApp()
            }
        }
    }
}

@Composable
fun BingSearchApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()   // 👈 thêm

    var keywords by remember { mutableStateOf(listOf<String>()) }
    var count by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    // Load keyword từ file assets
    LaunchedEffect(Unit) {
        keywords = context.assets.open("keywords.txt")
            .bufferedReader()
            .readLines()
            .filter { it.isNotBlank() }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Số lần search: $count")
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = keywords.isNotEmpty() && !isRunning,
                onClick = {
                    isRunning = true
                    scope.launch {              // 👈 sửa chỗ này
                        repeat(20) {
                            val keyword = keywords.random()
                            val url =
                                "https://www.bing.com/search?q=${keyword.replace(" ", "+")}&cvid=d8f3c92d44454ab0881167e651f43897&gs_lcrp=EgRlZGdlKgYIABBFGDkyBggAEEUYOTIGCAEQABhAMgYIAhAAGEAyBggDEAAYQDIGCAQQABhAMgYIBRAAGEAyBggGEAAYQDIGCAcQABhAMgYICBAAGEAyCAgJEOkHGPxV0gEIMTMzNGowajSoAgiwAgE&FORM=ANAB01&DAF0=1&PC=U531"
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)

                            count++
                            delay(Random.nextLong(5000, 10000))
                        }
                        isRunning = false
                    }
                }
            ) {
                Text(text = "Bắt đầu Bing Search")
            }
        }
    }
}
