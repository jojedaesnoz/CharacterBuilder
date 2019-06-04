package com.jojeda.creador_personajes.tareas_asincronas;

import android.os.AsyncTask;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.TareasCRUDListener;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.jojeda.creador_personajes.util.Constantes.URL_SERVIDOR;

public class EliminarPersonajeTask extends AsyncTask<Void, Void, Void> {

    private Personaje personaje;
    private TareasCRUDListener callbackListener;

    public EliminarPersonajeTask(TareasCRUDListener callbackListener, Personaje personaje) {
        this.personaje = personaje;
        this.callbackListener = callbackListener;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callbackListener.onBorrarPersonaje();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getForObject(URL_SERVIDOR + "/eliminar_personaje?id=" + personaje.getId(), Void.class);
        return null;
    }
}
