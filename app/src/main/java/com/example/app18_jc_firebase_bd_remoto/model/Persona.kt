package com.example.app18_jc_firebase_bd_remoto.model

// Firebase necesita valores por defecto (constructor vacío) para deserializar con getValue()
data class Persona(
    val id: String = "",
    val nombre: String = "",
    val edad: Int = 0
)