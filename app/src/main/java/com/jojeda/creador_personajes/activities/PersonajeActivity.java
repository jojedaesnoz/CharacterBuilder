package com.jojeda.creador_personajes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.R;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.OnPersonajeRecibidoListener;
import com.jojeda.creador_personajes.tareas_asincronas.RecibirPersonajeTask;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Intent.ACTION_VIEW;
import static com.jojeda.creador_personajes.util.Constantes.PERSONAJE;

import static com.jojeda.creador_personajes.Personaje.*;
import static com.jojeda.creador_personajes.util.Constantes.PUNTUACIONES;

public class PersonajeActivity extends AppCompatActivity implements View.OnClickListener, OnPersonajeRecibidoListener {

	private EditText etNombre;
	private Spinner spRaza, spClase, spNivel;
	private ArrayList<Spinner> spinnersAtributos;
	private ArrayList<Integer> valoresAtributos;
	private Button btGuardar, btCancelar;
	private Personaje personaje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personaje_layout);


		valoresAtributos = PUNTUACIONES;
		cargarViews();
		prepararInformacionPersonal();
		ponerListenersBotones();
		prepararAtributos();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);


		// ATTENTION: This was auto-generated to handle app links.
		Intent intent = getIntent();
		String appLinkAction = intent.getAction();

		if (appLinkAction != null && appLinkAction.equals(ACTION_VIEW)) {
			new RecibirPersonajeTask(this, intent.getData().toString()).execute();
		} else {
			personaje = (Personaje) intent.getSerializableExtra(PERSONAJE);
			if (personaje.getAtributos().isEmpty())
				personaje.setAtributos(atributosToString(PUNTUACIONES));
			cargarPersonaje(personaje);
		}
	}

	@Override
	public void onPersonajeRecibido(Personaje personaje) {
		if (personaje != null) {
			this.personaje = personaje;
			cargarPersonaje(personaje);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			onBackPressed();
		return true;
	}

	private void cargarViews() {
		etNombre = findViewById(R.id.etNombre);
		spRaza = findViewById(R.id.spRaza);
		spClase = findViewById(R.id.spClase);
		spNivel = findViewById(R.id.spNivel);
		spinnersAtributos = new ArrayList<>(Arrays.asList(new Spinner[]{
				findViewById(R.id.spFuerza), findViewById(R.id.spDestreza),
				findViewById(R.id.spConstitucion), findViewById(R.id.spInteligencia),
				findViewById(R.id.spSabiduria), findViewById(R.id.spCarisma)}));
		btGuardar = findViewById(R.id.btGuardar);
		btCancelar = findViewById(R.id.btCancelar);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btCancelar:
				onBackPressed();
				break;
			case R.id.btGuardar:
				extraerPersonaje();

				// Si se ha llegado desde un enlace externo, lanza una actividad, si no, cierra esta
				if (getIntent().getAction() != null && getIntent().getAction().equals(ACTION_VIEW)) {
					Intent intentResultado = new Intent(this, ListadoActivity.class);
					intentResultado.putExtra(PERSONAJE, personaje);
					startActivity(intentResultado);
				} else {
					Intent intentResultado = new Intent();
					intentResultado.putExtra(PERSONAJE, personaje);
					setResult(RESULT_OK, intentResultado);
					finish();
				}
				break;
			default:
				break;
		}
	}

	private void prepararInformacionPersonal() {
		spRaza.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Personaje.Raza.values()));
		spClase.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Personaje.Clase.values()));
		spNivel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
	}


	public void extraerPersonaje() {
		String nombre = etNombre.getText().toString();
		int nivel = (int) spNivel.getSelectedItem();
		Raza raza = (Personaje.Raza) spRaza.getSelectedItem();
		Clase clase = (Clase) spClase.getSelectedItem();

		personaje.setNombre(nombre);
		personaje.setRaza(raza);
		personaje.setClase(clase);
		personaje.setNivel(nivel);
		personaje.setAtributos(atributosToString(valoresAtributos));
	}

	// Convertir la cadena a lista de enteros
	public ArrayList<Integer> stringToAtributos(String cadena) {
		ArrayList<Integer> atributos = new ArrayList<>();
		for (String valor : cadena.split("_")) {
			atributos.add(Integer.parseInt(valor));
		}
		return atributos;
	}

	// Convertir la lista de enteros a cadena
	public String atributosToString(ArrayList<Integer> lista) {
		StringBuilder atributos = new StringBuilder();
		atributos.append(lista.get(0));
		for (int i = 1; i < lista.size(); i++) {
			atributos.append("_");
			atributos.append(lista.get(i));
		}
		return atributos.toString();
	}

	public void cargarPersonaje(Personaje personaje) {
		etNombre.setText(personaje.getNombre());
		spRaza.setSelection(Arrays.asList(Raza.values()).indexOf(personaje.getRaza()));
		spClase.setSelection(Arrays.asList(Clase.values()).indexOf(personaje.getClase()));
		valoresAtributos = stringToAtributos(personaje.getAtributos());

		for (int i = 0; i < valoresAtributos.size(); i++) {
			spinnersAtributos.get(i).setSelection(PUNTUACIONES.indexOf(valoresAtributos.get(i)));
		}
	}

	private void prepararAtributos() {
		// Crear el listener para las selecciones en los spinner
		AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

			/*
			Cuando se selecciona un valor en un spinner, el spinner que antes lo contenia
			pasa a tener el valor del spinner seleccionado. Es decir, se intercambian los valores
			de forma que no se repita ninguno
			 */
			boolean cambiado = false;

			@Override
			public void onItemSelected(AdapterView<?> spinnerSeleccionado, View view, int pos, long l) {
				if (!cambiado) {
					int indiceSeleccionado = spinnersAtributos.indexOf(spinnerSeleccionado);
					int valorAntiguo = valoresAtributos.get(indiceSeleccionado);
					int valorNuevo = valoresAtributos.get(pos);
					int indiceAntiguo = valoresAtributos.indexOf(valorNuevo);

					spinnersAtributos.get(indiceAntiguo).setSelection(valoresAtributos.indexOf(valorAntiguo));
					valoresAtributos.set(indiceSeleccionado, valorNuevo);
					valoresAtributos.set(indiceAntiguo, valorAntiguo);
					System.out.println(Arrays.toString(valoresAtributos.toArray()));
					cambiado = true;
				} else cambiado = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}
		};

		// Rellenar los spinners y ponerles los listeners
		for (int i = 0; i < spinnersAtributos.size(); i++) {
			spinnersAtributos.get(i).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PUNTUACIONES));
			spinnersAtributos.get(i).setSelection(i);
			spinnersAtributos.get(i).setOnItemSelectedListener(listener);
		}
	}

	private void ponerListenersBotones() {
		btCancelar.setOnClickListener(this);
		btGuardar.setOnClickListener(this);
	}

}
