package com.jojeda.creador_personajes.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jojeda.creador_personajes.Personaje;
import com.jojeda.creador_personajes.R;

import java.util.List;

public class PersonajesAdapter extends BaseAdapter {

    private final List<Personaje> personajes;
    private final int layout;
    private Context context;
    private PersonajeViewHolder holder;


    public PersonajesAdapter(List<Personaje> personajes, Context context) {
        this.personajes = personajes;
        this.context = context;
        layout = R.layout.item_lista_personajes;
    }

    public PersonajeViewHolder getHolder() {
        return holder;
    }

    @Override
    public Object getItem(int i) {
        return personajes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layout, null);

            holder = new PersonajeViewHolder();
            holder.tvNombre = view.findViewById(R.id.tvListaNombre);
            holder.tvRaza = view.findViewById(R.id.tvListaRaza);
            holder.tvClase = view.findViewById(R.id.tvListaClase);
            holder.tvNivel = view.findViewById(R.id.tvListaNivel);

            view.setTag(holder);
        } else {
            holder = (PersonajeViewHolder) view.getTag();
        }

        Personaje personaje = personajes.get(i);
        holder.tvNombre.setText(personaje.getNombre());
        holder.tvRaza.setText(personaje.getRaza().toString());
        holder.tvClase.setText(personaje.getClase().toString());
        holder.tvNivel.setText(String.valueOf(personaje.getNivel()));

        return view;
    }

    public static class PersonajeViewHolder {
        public TextView tvNombre, tvRaza, tvClase, tvNivel;
    }

    @Override
    public int getCount() {
        return personajes.size();
    }
}
