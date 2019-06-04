package com.jojeda.creador_personajes;

import java.io.Serializable;

import static com.jojeda.creador_personajes.Personaje.Clase.GUERRERO;
import static com.jojeda.creador_personajes.Personaje.Raza.HUMANO;


public class Personaje implements Serializable {

    public enum Raza {
        ENANO, ELFO, HUMANO, MEDIANO
    }
    public enum Clase {
        GUERRERO, MAGO, PALADIN, LADRON
    }

    // Atributos de clase
    private long id;
    private String nombre;
    private Raza raza;
    private Clase clase;
    private int nivel;
    private String atributos;

    // Inicializacion
    {
    	atributos = "";
        raza = HUMANO;
        clase = GUERRERO;
    }

    public Personaje() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Personaje(String nombre, Raza raza, Clase clase, int nivel, String atributos) {
        this.nombre = nombre;
        this.raza = raza;
        this.clase = clase;
        this.atributos = atributos;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getAtributos() {
        return atributos;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public static class Usuario {
        private long id;
        private String nombre;
        private String contrasena;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }
}
