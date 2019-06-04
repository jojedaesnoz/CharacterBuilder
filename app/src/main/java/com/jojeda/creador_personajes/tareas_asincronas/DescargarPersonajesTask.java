package com.jojeda.creador_personajes.tareas_asincronas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.R;
import com.jojeda.creador_personajes.activities.ListadoActivity;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.OnLoginCallbackListener;
import com.jojeda.creador_personajes.tareas_asincronas.interfaces.TareasCRUDListener;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.jojeda.creador_personajes.util.Constantes.URL_SERVIDOR;

public class DescargarPersonajesTask extends AsyncTask<Void, Void, Void> {
    private TareasCRUDListener callbackListener;
    private List<Personaje> personajes;

    public DescargarPersonajesTask(TareasCRUDListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callbackListener.onDescargarPersonajes(personajes);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        personajes = Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + "/personajes", Personaje[].class));
        return null;
    }

    public static class RegistrarUsuarioTask extends AsyncTask<Void, Void, Void> {

        private OnLoginCallbackListener listener;
        private Personaje.Usuario usuario;

        public RegistrarUsuarioTask(OnLoginCallbackListener listener, Personaje.Usuario usuario) {
            this.listener = listener;
            this.usuario = usuario;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getForObject(URL_SERVIDOR +
                    "/registrar_usuario?id=" + usuario.getId() +
                    "&nombre=" + usuario.getNombre() +
                    "&contrasena=" + usuario.getContrasena(), Void.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.onRegistrarUsuario();
        }

        public static class LoginActivity extends AppCompatActivity implements OnLoginCallbackListener  {

            private EditText etUsuario, etContrasena;
            private Button btRegistrar, btIniciarSesion;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                cargarViews();
                ponerListeners();
            }

            private void cargarViews() {
                etUsuario = findViewById(R.id.etUsuario);
                etContrasena = findViewById(R.id.etContrasena);
                btRegistrar = findViewById(R.id.btRegistrar);
                btIniciarSesion = findViewById(R.id.btIniciarSesion);
            }

            private void ponerListeners() {
                btRegistrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registrarUsuario();
                    }
                });
                btIniciarSesion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iniciarSesion();
                    }
                });
            }

            private void iniciarSesion() {
                Personaje.Usuario usuario = new Personaje.Usuario();
                usuario.setNombre(etUsuario.getText().toString());
                usuario.setContrasena(etContrasena.getText().toString());

                new IniciarSesionTask(this, usuario).execute();
            }

            private void registrarUsuario() {
                Personaje.Usuario usuario = new Personaje.Usuario();
                usuario.setNombre(etUsuario.getText().toString());
                usuario.setContrasena(etContrasena.getText().toString());

                new RegistrarUsuarioTask(this, usuario).execute();
            }

            @Override
            public void onRegistrarUsuario() {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListadoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onIniciarSesion(boolean resultado) {
                if (resultado) {
                    Intent intent = new Intent(this, ListadoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Error en el nombre de usuario o contraseña", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static class IniciarSesionTask extends AsyncTask<Void, Void, Void> {

        private OnLoginCallbackListener listener;
        private Personaje.Usuario usuario;
        private Personaje.Usuario resultado;

        public IniciarSesionTask(OnLoginCallbackListener listener, Personaje.Usuario usuario) {
            this.listener = listener;
            this.usuario = usuario;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            resultado = restTemplate.getForObject(URL_SERVIDOR +
                            "/iniciar_sesion?nombre=" + usuario.getNombre() +
                            "&contrasena=" + usuario.getContrasena(), Personaje.Usuario.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.onIniciarSesion(resultado != null);
        }
    }
}