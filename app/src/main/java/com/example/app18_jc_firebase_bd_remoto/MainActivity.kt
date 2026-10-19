package com.example.app18_jc_firebase_bd_remoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.app18_jc_firebase_bd_remoto.ui.theme.App18_jc_firebase_bd_remotoTheme
import com.example.app18_jc_firebase_bd_remoto.view.PantallaPersona

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            App18_jc_firebase_bd_remotoTheme {
                PantallaPersona()
            }
        }
    }
}