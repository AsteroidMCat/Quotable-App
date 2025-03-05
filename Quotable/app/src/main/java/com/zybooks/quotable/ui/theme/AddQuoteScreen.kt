package com.zybooks.quotable.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun AddQuoteScreen(
    addQuote: (String, String) -> Unit = { _, _ -> },
) {
    AddQuote(onEnterQuote = addQuote)
}

@Composable
fun AddQuote(onEnterQuote: (String, String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var quoteBody by remember { mutableStateOf("") }
    var quoteAuthor by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            "Add Quote",
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .align(Alignment.Start),
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            value = quoteBody,
            onValueChange = { quoteBody = it },
            label = { Text("Enter Quote") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            value = quoteAuthor,
            onValueChange = { quoteAuthor = it },
            label = { Text("Enter Author") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onEnterQuote(quoteBody, quoteAuthor)
                    quoteBody = ""
                    quoteAuthor = ""
                    keyboardController?.hide()
                }
            )
        )
        Button(modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            onClick = {
                onEnterQuote(quoteBody, quoteAuthor)
                quoteBody = ""
                quoteAuthor = ""
            }
        ) {
            Text(text = "Add Quote")
        }
    }

}
