package com.example.app18_jc_firebase_bd_remoto.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app18_jc_firebase_bd_remoto.model.Persona
import com.example.app18_jc_firebase_bd_remoto.viewmodel.PersonaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPersona(viewModel: PersonaViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.mensajeMostrado()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Personas - Firebase") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = state.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.edad,
                onValueChange = viewModel::onEdadChange,
                label = { Text("Edad") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Button(onClick = viewModel::guardar) {
                    Text(if (state.personaEditando == null) "Guardar" else "Actualizar")
                }
                if (state.personaEditando != null) {
                    OutlinedButton(onClick = viewModel::limpiarFormulario) { Text("Cancelar") }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (state.cargando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(state.personas, key = { it.id }) { persona ->
                        PersonaItem(
                            persona = persona,
                            onEditar = { viewModel.seleccionarParaEditar(persona) },
                            onEliminar = { viewModel.eliminar(persona) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PersonaItem(persona: Persona, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("Nombre: ${persona.nombre}", style = MaterialTheme.typography.titleMedium)
                Text("Edad: ${persona.edad}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEditar) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
            IconButton(onClick = onEliminar) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
        }
    }
}