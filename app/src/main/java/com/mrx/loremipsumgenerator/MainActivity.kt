package com.mrx.loremipsumgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.mrx.loremipsumgenerator.ui.theme.LoremIpsumGeneratorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoremIpsumGeneratorTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val totalWords = rememberSaveable { mutableStateOf("") }
    val loremIpsum = rememberSaveable { mutableStateOf("") }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState, Modifier.imePadding()) },
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            item {
                Box(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = totalWords.value,
                        onValueChange = { newTotalWords ->
                            totalWords.value = newTotalWords
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Total Words")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        if (totalWords.value.isNotEmpty()) {
                            loremIpsum.value = LoremIpsum(totalWords.value.toInt()).values.first()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Please enter the total number of words"
                                )
                            }
                        }
                    }) {
                    Text(text = "Generate!!!")
                }
                if (loremIpsum.value.isNotEmpty()) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            clipboardManager.setText(AnnotatedString(loremIpsum.value))
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Successfully Copied"
                                )
                            }
                        }) {
                        Text(text = "Copy")
                    }
                    Text(
                        text = loremIpsum.value,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoremIpsumGeneratorTheme {
        HomeScreen()
    }
}