package com.fabricius.app.paisesapp.model;

import android.provider.ContactsContract;

import com.fabricius.app.paisesapp.helper.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Pais implements Serializable {

    private String nome;
    private String capital;
    private String continente;
    private String key;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

    public void salvarPais(){

        DatabaseReference firebasereference = ConfigFirebase.getDatabasereference();
        firebasereference.child("pais")
                .push()
                .setValue(this);
    }
}
