package com.example.app18_jc_firebase_bd_remoto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app18_jc_firebase_bd_remoto.model.Persona
import com.example.app18_jc_firebase_bd_remoto.model.PersonaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonaViewModel(
    private val repository: PersonaRepository = PersonaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonaUiState())
    val uiState: StateFlow<PersonaUiState> = _uiState.asStateFlow()

    init {
        escucharPersonas()
    }

    // Se suscribe una sola vez; la lista se actualiza sola (tiempo real)
    private fun escucharPersonas() {
        viewModelScope.launch {
            repository.obtenerPersonas()
                .catch { e -> _uiState.update { it.copy(cargando = false, mensaje = "Error: ${e.message}") } }
                .collect { lista -> _uiState.update { it.copy(personas = lista, cargando = false) } }
        }
    }

    fun onNombreChange(valor: String) = _uiState.update { it.copy(nombre = valor) }

    fun onEdadChange(valor: String) {
        if (valor.all { it.isDigit() }) _uiState.update { it.copy(edad = valor) }
    }

    fun guardar() {
        val estado = _uiState.value
        val nombre = estado.nombre.trim()
        val edad = estado.edad.toIntOrNull()

        if (nombre.isEmpty() || edad == null) {
            _uiState.update { it.copy(mensaje = "Completa nombre y edad") }
            return
        }

        viewModelScope.launch {
            try {
                val editando = estado.personaEditando
                if (editando == null) {
                    repository.insertar(nombre, edad)
                    mostrarMensaje("Persona registrada")
                } else {
                    repository.actualizar(editando.copy(nombre = nombre, edad = edad))
                    mostrarMensaje("Persona actualizada")
                }
                limpiarFormulario()
            } catch (e: Exception) {
                mostrarMensaje("Error: ${e.message}")
            }
        }
    }

    fun seleccionarParaEditar(persona: Persona) = _uiState.update {
        it.copy(personaEditando = persona, nombre = persona.nombre, edad = persona.edad.toString())
    }

    fun eliminar(persona: Persona) {
        viewModelScope.launch {
            try {
                repository.eliminar(persona.id)
                mostrarMensaje("Persona eliminada")
            } catch (e: Exception) {
                mostrarMensaje("Error: ${e.message}")
            }
        }
    }

    fun limpiarFormulario() = _uiState.update {
        it.copy(nombre = "", edad = "", personaEditando = null)
    }

    fun mensajeMostrado() = _uiState.update { it.copy(mensaje = null) }

    private fun mostrarMensaje(texto: String) = _uiState.update { it.copy(mensaje = texto) }
}