package com.zybooks.quotable.ui.theme

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteFavoriteQuotesDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    AlertDialog(
        onDismissRequest ={
            onDismiss()
        },
        title ={
            Text("Delete all quotes?")
        },
        confirmButton={
            Button(onClick ={onConfirm()}){
                Text("Yes")
            }
        },
        dismissButton ={
            Button(onClick = {onDismiss()}){
                Text("No")
            }
        }
    )
}