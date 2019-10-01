package com.fabricius.app.paisesapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fabricius.app.paisesapp.R;
import com.fabricius.app.paisesapp.model.Pais;

import java.util.List;


public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.MyViewHolder>{


    List<Pais> listPaisFav;
    Context c ;

    public FavoritosAdapter(List<Pais> listfavPais , Context c){
        this.listPaisFav = listfavPais;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.pais_informacoes, parent , false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pais pais = listPaisFav.get(position);

        holder.textFavPais.setText(pais.getNome());
        holder.textFavCapital.setText(pais.getCapital());
        holder.textFavContinente.setText(pais.getContinente());

    }

    @Override
    public int getItemCount() {
        return listPaisFav.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textFavPais, textFavCapital, textFavContinente;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textFavPais = itemView.findViewById(R.id.textViewNome);
            textFavCapital = itemView.findViewById(R.id.textViewCapital);
            textFavContinente = itemView.findViewById(R.id.textViewContinente);

        }
    }
}
