package com.neoqee.message

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.neoqee.message.ui.theme.MessageTheme

class MainActivity : ComponentActivity() {

    var text = mutableStateOf("")
    val list = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Greeting("Android")
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(modifier = Modifier.fillMaxHeight(0.8f).apply {
                            width(IntrinsicSize.Max)
                            background(Color.Blue)
                        }) {
                            items(list.size) { idx ->
                                SelectionContainer {
                                    Text(text = list[idx])
                                }
                            }
                        }
                        Row(modifier = Modifier.fillMaxSize().apply {
                            width(IntrinsicSize.Max)
                            background(MaterialTheme.colors.onPrimary)
                        }) {
                            TextField(
                                value = text.value,
                                onValueChange = { text ->
                                    this@MainActivity.text.value = text
                                },
                                modifier = Modifier.fillMaxWidth(0.5f).apply {
                                    height(IntrinsicSize.Max)
                                },
                            )
                            TextButton(
                                onClick = {
                                    list.add("send -> ${text.value}")
                                    WebSocketClient.send(text.value)
                                },
                                modifier = Modifier.fillMaxWidth().apply {},
                            ) {
                                Text(text = "Send", color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        WebSocketClient.start {
            list.add("recv -> $it")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MessageTheme {
        Greeting("Android")
    }
}