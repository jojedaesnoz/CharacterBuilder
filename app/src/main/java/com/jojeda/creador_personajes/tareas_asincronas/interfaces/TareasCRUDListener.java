package com.jojeda.creador_personajes.tareas_asincronas.interfaces;

import com.jojeda.creador_personajes.Personaje;

import java.util.List;

public interface TareasCRUDListener {
    void onDescargarPersonajes(List<Personaje> personajes);
    void onBorrarPersonaje();
    void onGuardarPersonaje();
}
