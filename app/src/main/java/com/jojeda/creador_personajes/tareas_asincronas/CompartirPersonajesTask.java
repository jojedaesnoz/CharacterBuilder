package com.jojeda.creador_personajes.tareas_asincronas;

import android.os.AsyncTask;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.TareasCRUDListener;

public class CompartirPersonajesTask extends AsyncTask<Void, Void, Void> {

    private final TareasCRUDListener callbackListener;
    private final Personaje personaje;

    public CompartirPersonajesTask(TareasCRUDListener callbackListener, Personaje personaje) {
        this.callbackListener = callbackListener;
        this.personaje = personaje;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}
