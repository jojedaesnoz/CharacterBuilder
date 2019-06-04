package com.jojeda.creador_personajes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.R;
import com.jojeda.creador_personajes.tareas_asincronas.DescargarPersonajesTask;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.TareasCRUDListener;
import com.jojeda.creador_personajes.tareas_asincronas.EliminarPersonajeTask;
import com.jojeda.creador_personajes.tareas_asincronas.GuardarPersonajeTask;
import com.jojeda.creador_personajes.util.PersonajesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.jojeda.creador_personajes.util.Constantes.CODIGO_CREAR_PERSONAJE;
import static com.jojeda.creador_personajes.util.Constantes.CODIGO_EDITAR_PERSONAJE;
import static com.jojeda.creador_personajes.util.Constantes.PERSONAJE;
import static com.jojeda.creador_personajes.util.Constantes.URL_SERVIDOR;

public class ListadoActivity extends AppCompatActivity implements TareasCRUDListener {

    private ListView listaPersonajes;
    private PersonajesAdapter adapter;
    private List<Personaje> personajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado);

		personajes = new ArrayList<>();
		prepararListView();
		descargarPersonajes();
	}


	private void prepararListView() {
        adapter = new PersonajesAdapter(personajes, this);
        listaPersonajes = findViewById(R.id.lvPersonajes);
        listaPersonajes.setAdapter(adapter);
        registerForContextMenu(listaPersonajes);
        findViewById(R.id.btAddPersonaje).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PersonajeActivity.class);
                intent.putExtra(PERSONAJE, new Personaje());
                startActivityForResult(intent, CODIGO_CREAR_PERSONAJE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == CODIGO_CREAR_PERSONAJE || requestCode == CODIGO_EDITAR_PERSONAJE) {
            Personaje personaje = (Personaje) data.getSerializableExtra(PERSONAJE);
            guardarPersonaje(personaje);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_lista, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = menuInfo.position;
        Personaje personaje = personajes.get(position);
        Intent intent;

        switch (item.getItemId()) {
            case R.id.btListaCompartir:
                compartirPersonaje(personaje);
                break;
            case R.id.btListaEditar:
                intent = new Intent(this, PersonajeActivity.class);
                intent.putExtra(PERSONAJE, personaje);
                startActivityForResult(intent, CODIGO_EDITAR_PERSONAJE);
                break;
            case R.id.btListaEliminar:
                eliminarPersonaje(personaje);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    // Metodos para lanzar las tareas asincronas
    private void eliminarPersonaje(Personaje personaje) {
        //todo:activar
        new EliminarPersonajeTask(this, personaje).execute();

//        personajes.remove(personaje);
//        adapter.notifyDataSetChanged();
    }

    private void guardarPersonaje(Personaje personaje) {
//        personajes.add(personaje);
//        adapter.notifyDataSetChanged();
//
//        //todo:activar
        new GuardarPersonajeTask(this, personaje).execute();
    }

    private void compartirPersonaje(Personaje personaje) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String enlace = URL_SERVIDOR + "/get_personaje?id=" + personaje.getId();
        sendIntent.putExtra(Intent.EXTRA_TEXT, enlace);
        sendIntent.setType("text/plain");

        String titulo = "Compartir con";
        Intent chooser = Intent.createChooser(sendIntent, titulo);
        if(sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }

    }

    private void descargarPersonajes() {
        //todo:activar
        new DescargarPersonajesTask(this).execute();
    }

    // Callbacks de las tareas asincronas
    @Override
    public void onDescargarPersonajes(List<Personaje> personajes) {
    	if (personajes != null) {
			this.personajes.clear();
			this.personajes.addAll(personajes);
			adapter.notifyDataSetChanged();
		}

    }

    @Override
    public void onBorrarPersonaje() {
        descargarPersonajes();
    }

    @Override
    public void onGuardarPersonaje() {
        descargarPersonajes();
    }
}
