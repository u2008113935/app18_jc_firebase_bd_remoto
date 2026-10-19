package com.example.app18_jc_firebase_bd_remoto.viewmodel

import com.example.app18_jc_firebase_bd_remoto.model.Persona

// Estado único que la vista observa
data class PersonaUiState(
    val personas: List<Persona> = emptyList(),
    val nombre: String = "",
    val edad: String = "",
    val personaEditando: Persona? = null,   // null = modo insertar
    val cargando: Boolean = true,
    val mensaje: String? = null
)