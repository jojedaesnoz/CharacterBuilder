package com.jojeda.creador_personajes.tareas_asincronas;

import android.os.AsyncTask;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.OnPersonajeRecibidoListener;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RecibirPersonajeTask extends AsyncTask<Void, Void, Void> {

	private OnPersonajeRecibidoListener listener;
	private String url;
	private Personaje personaje;

	public RecibirPersonajeTask(OnPersonajeRecibidoListener listener, String url) {
		this.listener = listener;
		this.url = url;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		listener.onPersonajeRecibido(personaje);
	}

	@Override
    protected Void doInBackground(Void... voids) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        personaje = restTemplate.getForObject(url, Personaje.class);
        return null;
    }
}