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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private EditText editCadastroNome, editCadastroEmail, editCadastroSenha;
    private Button btnCadastrar;
    private ProgressBar progressBar;

    private Usuario usuario;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        editCadastroNome = findViewById(R.id.editCadastrarNome);
        editCadastroEmail = findViewById(R.id.editCadastrarEmail);
        editCadastroSenha = findViewById(R.id.editCadastrarPass);
        progressBar = findViewById(R.id.progressBarCadstrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);


        progressBar.setVisibility(View.GONE);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cadastrarCliente();
            }
        });

    }

    public void cadastrarCliente() {

        if (validaCampos() == true) {
            usuario = new Usuario();
            usuario.setNome(editCadastroNome.getText().toString());
            usuario.setEmail(editCadastroEmail.getText().toString());
            usuario.setSenha(editCadastroSenha.getText().toString());

            auth = ConfigFirebase.getAuthreference();

            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(
                    this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CadastroActivity.this, "Bem-vindo " + usuario.getNome(), Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);

                                String exececao = "";

                                try {
                                    throw task.getException();

                                } catch (FirebaseAuthWeakPasswordException e) {
                                    exececao = "Digite uma senha mais forte";
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    exececao = "Email Invalido";
                                } catch (FirebaseAuthUserCollisionException e) {
                                    exececao = "Email já Cadastrado";

                                } catch (Exception e) {
                                    exececao = "Erro ao cadastrar usuario " + e.getMessage();
                                    e.printStackTrace();
                                }

                                Toast.makeText(CadastroActivity.this, exececao, Toast.LENGTH_SHORT).show();

                            }

                        }

                    });

        }
    }


    public Boolean validaCampos() {

        String editNome = editCadastroNome.getText().toString();
        String editEmail = editCadastroEmail.getText().toString();
        String editSenha = editCadastroSenha.getText().toString();

        if (!editNome.isEmpty()) {
            if (!editEmail.isEmpty()) {
                if (!editSenha.isEmpty()) {

                    return true;

                } else {
                    Toast.makeText(CadastroActivity.this, "Campo Senha Vazio", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(CadastroActivity.this, "Campo Email Vazio", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(CadastroActivity.this, "Campo Nome Vazio", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}