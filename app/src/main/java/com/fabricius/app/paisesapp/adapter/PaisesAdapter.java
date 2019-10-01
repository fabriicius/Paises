package com.fabricius.app.paisesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fabricius.app.paisesapp.R;
import com.fabricius.app.paisesapp.model.Pais;


import java.util.List;


public class PaisesAdapter  extends RecyclerView.Adapter<PaisesAdapter.MyViewHolder> {

    List<Pais> listPais;
    Context context;

    public PaisesAdapter(List<Pais> pais, Context context){
        this.listPais = pais;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.pais_informacoes, parent , false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pais pais = listPais.get(position);

        holder.textViewNome.setText(pais.getNome());
        holder.textViewCapital.setText(pais.getCapital());
        holder.textViewContinente.setText(pais.getContinente());


    }

    @Override
    public int getItemCount() {

        return listPais.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewNome, textViewCapital, textViewContinente;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewCapital = itemView.findViewById(R.id.textViewCapital);
            textViewContinente = itemView.findViewById(R.id.textViewContinente);
        }
    }
}
