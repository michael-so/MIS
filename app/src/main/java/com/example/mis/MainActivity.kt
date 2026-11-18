package com.example.mis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mis.ui.screens.StudentManagementApp
import com.example.mis.ui.theme.MISTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MISTheme {
                StudentManagementApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentManagementAppPreview() {
    MISTheme {
        StudentManagementApp()
    }
}
