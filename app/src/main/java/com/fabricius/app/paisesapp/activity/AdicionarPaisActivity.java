package com.fabricius.app.paisesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fabricius.app.paisesapp.R;
import com.fabricius.app.paisesapp.model.Pais;

public class AdicionarPaisActivity extends AppCompatActivity {

    private EditText textAdcionarNome, textAdicionarCapital, textAdicionarContinente;
    private ProgressBar progressBarSalvar;
    private Button btnSalvar;
    private Pais pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pais);

        textAdcionarNome =  findViewById(R.id.textAdicionarNome);
        textAdicionarCapital = findViewById(R.id.textAdicionarCapital);
        textAdicionarContinente = findViewById(R.id.textAdicionarContinente);
        progressBarSalvar = findViewById(R.id.progressSalvarBar);
        btnSalvar = findViewById(R.id.btn_salvarPais);


    }

    public void salvarDados(View vi){

        progressBarSalvar.setVisibility(View.GONE);
        if(validaCampos()==true){

            pais = new Pais();
            pais.setNome(textAdcionarNome.getText().toString());
            pais.setCapital(textAdicionarCapital.getText().toString());
            pais.setContinente(textAdicionarContinente.getText().toString());

            progressBarSalvar.setVisibility(View.VISIBLE);
            pais.salvarPais();

            startActivity(new Intent(AdicionarPaisActivity.this, MainActivity.class));
            finish();
        }

    }

    public Boolean validaCampos(){
        if(!textAdcionarNome.getText().toString().isEmpty()){
            if(!textAdicionarCapital.getText().toString().isEmpty()){
                if (!textAdicionarContinente.getText().toString().isEmpty()){
                    return true;

                }else {
                    Toast.makeText(this, "Campo Continente vazio", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(this, "Campo Capital vazio", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            Toast.makeText(this, "Campo nome vazio", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
