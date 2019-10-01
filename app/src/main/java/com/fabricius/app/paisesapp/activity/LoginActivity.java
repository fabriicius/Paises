package com.fabricius.app.paisesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.fabricius.app.paisesapp.R;
import com.fabricius.app.paisesapp.helper.ConfigFirebase;
import com.fabricius.app.paisesapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editLoginEmail,editLoginSenha;
    private Button btnLogar;
    private ProgressBar progressBar;

    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginSenha = findViewById(R.id.editLogarPass);
        btnLogar = findViewById(R.id.btnLogar);
        progressBar = findViewById(R.id.progressBarLogin);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarCliente();
            }
        });

        recuperaUsuarioLogado();

    }

    public void logarCliente(){

        if(validarCampos()== true){
            usuario = new Usuario();
            usuario.setEmail(editLoginEmail.getText().toString());
            usuario.setSenha(editLoginSenha.getText().toString());

            auth = ConfigFirebase.getAuthreference();


            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(
                            this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this , "Bem vindo novamente" , Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this , "Erro ao fazer login" , Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }
                    );

        }

    }



    public boolean validarCampos(){

        String textEmail = editLoginEmail.getText().toString();
        String textSenha = editLoginSenha.getText().toString();

        if(!textEmail.isEmpty()){
            if(!textSenha.isEmpty()){
                return true;
            }else{
                Toast.makeText(LoginActivity.this , "Digite uma senha válida" , Toast.LENGTH_SHORT).show();
                return false;
            }

        }else {
            Toast.makeText(LoginActivity.this , "Digite um email válido" , Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void abrirCadastro(View vi){
        Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(i);
    }

    public void recuperaUsuarioLogado(){

        auth = ConfigFirebase.getAuthreference();

        if(auth.getCurrentUser() != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }


    }
}