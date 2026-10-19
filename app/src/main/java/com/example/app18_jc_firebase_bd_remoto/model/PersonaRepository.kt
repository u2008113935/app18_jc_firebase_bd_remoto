package com.example.app18_jc_firebase_bd_remoto.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Repository: única capa que conoce el SDK de Firebase
class PersonaRepository {

    private val db = FirebaseDatabase.getInstance()
    private val personasRef = db.getReference("personas")

    // CREATE
    suspend fun insertar(nombre: String, edad: Int) {
        val id = personasRef.push().key ?: return
        val persona = Persona(id = id, nombre = nombre, edad = edad)
        personasRef.child(id).setValue(persona).await()
    }

    // UPDATE
    suspend fun actualizar(persona: Persona) {
        personasRef.child(persona.id).setValue(persona).await()
    }

    // DELETE
    suspend fun eliminar(id: String) {
        personasRef.child(id).removeValue().await()
    }

    // READ en tiempo real: emite la lista cada vez que cambia algo en la nube
    fun obtenerPersonas(): Flow<List<Persona>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { it.getValue(Persona::class.java) }
                trySend(lista)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        personasRef.addValueEventListener(listener)
        awaitClose { personasRef.removeEventListener(listener) }
    }
}