package com.fabricius.app.paisesapp.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fabricius.app.paisesapp.R;
import com.fabricius.app.paisesapp.adapter.PaisesAdapter;
import com.fabricius.app.paisesapp.helper.ConfigFirebase;
import com.fabricius.app.paisesapp.helper.RecyclerItemClickListener;

import com.fabricius.app.paisesapp.model.Pais;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnFavoritos;
    private Button btnAdicionarPais, btnPaisFavoritos;

    private RecyclerView recyclerView;

    private DatabaseReference firebaseReference = ConfigFirebase.getDatabasereference();

    private DatabaseReference referencePais;

    private PaisesAdapter adapter;

    private List<Pais> listPais = new ArrayList<>();
    private Pais pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdicionarPais = findViewById(R.id.btn_AdicionarPais);
       // btnPaisFavoritos = findViewById(R.id.btn_PaisesFavoritos);
        recyclerView = findViewById(R.id.reclyclerViewPrincipal);
       // btnFavoritos = findViewById(R.id.btn_PaisesFavoritos);

        btnAdicionarPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdicionarPaisActivity.class));
            }
        });




        configuracoesRecyclerView();
        recuperarPais();
        swipe();


    }

    public void configuracoesRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PaisesAdapter(listPais, this);
        recyclerView.setAdapter(adapter);

        //Evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        Pais paisSelecionado = listPais.get(position);
                        Intent i = new Intent(MainActivity.this, FavoritosActivity.class);
                        i.putExtra("PaisFavorito", paisSelecionado);
                        Toast.makeText(MainActivity.this, "Adicionado aos favoritos" , Toast.LENGTH_SHORT).show();
                        startActivity(i);

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sair:
                auth = ConfigFirebase.getAuthreference();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void recuperarPais() {

        referencePais = firebaseReference.child("pais");

        referencePais.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listPais.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){

                    Pais pais = dados.getValue(Pais.class);
                    pais.setKey(dados.getKey());
                    listPais.add(pais);
                }

                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void swipe(){
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

    public void excluirPais(final RecyclerView.ViewHolder viewHolder){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Exluir Pais ?");
        alertDialog.setMessage("Você tem certeza que deseja excluir esse pais ?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();

                //Recupera posição
                pais = listPais.get(position);

                //seleciona qual no começar
                referencePais = firebaseReference.child("pais");

                //remove a partir do iD
                referencePais.child(pais.getKey()).removeValue();

                adapter.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this , "Cancelado", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
      }

}
