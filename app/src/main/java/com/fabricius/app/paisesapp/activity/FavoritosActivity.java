package com.fabricius.app.paisesapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fabricius.app.paisesapp.R;
import com.fabricius.app.paisesapp.adapter.FavoritosAdapter;
import com.fabricius.app.paisesapp.helper.ConfigFirebase;
import com.fabricius.app.paisesapp.helper.RecyclerItemClickListener;
import com.fabricius.app.paisesapp.model.FavoritoPais;
import com.fabricius.app.paisesapp.model.Pais;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FavoritosAdapter adapter;

    private DatabaseReference referenceFavPais;
    private DatabaseReference firebaseReference = ConfigFirebase.getDatabasereference();

    private Pais paisDestinatario;
    private List<Pais> listPaisFav = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        recyclerView = findViewById(R.id.reclyclerViewFavoritos);

        configuracoesRecyclerView();

        swipe();

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarPaisFavorito();
    }

    public void configuracoesRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        //Bundle
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            paisDestinatario = (Pais) bundle.getSerializable("PaisFavorito");


            listPaisFav.add(paisDestinatario);

            adapter = new FavoritosAdapter(listPaisFav, getApplicationContext());

            DatabaseReference firebasereference = ConfigFirebase.getDatabasereference();
            firebasereference.child("paisFavoritos")
                    .push()
                    .setValue(paisDestinatario);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }



    }
    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_DRAG;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                excluirPais(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView( recyclerView );
    }

    public void recuperarPaisFavorito(){

        referenceFavPais = firebaseReference.child("paisFavoritos");

        referenceFavPais.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listPaisFav.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()) {

                    paisDestinatario = dados.getValue(Pais.class);
                    paisDestinatario.setKey(dados.getKey());
                    listPaisFav.add(paisDestinatario);

                }

                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

       public void excluirPais(final RecyclerView.ViewHolder viewHolder){
           AlertDialog.Builder alertDialog = new AlertDialog.Builder(FavoritosActivity.this);
           alertDialog.setTitle("Exluir Pais ?");
           alertDialog.setMessage("Você tem certeza que deseja excluir esse pais ?");
           alertDialog.setCancelable(false);


           alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   int position = viewHolder.getAdapterPosition();


                   //Recupera posição
                   paisDestinatario = listPaisFav.get(position);

                   //seleciona qual no começar
                   referenceFavPais = firebaseReference.child("paisFavoritos");

                   //remove a partir do iD
                   referenceFavPais.child(paisDestinatario.getKey()).removeValue();

                   adapter.notifyItemRemoved(position);
               }
           });

           alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   Toast.makeText(FavoritosActivity.this , "Cancelado", Toast.LENGTH_SHORT).show();
                   adapter.notifyDataSetChanged();
               }
           });


           AlertDialog alert = alertDialog.create();
           alert.show();
       }
}
