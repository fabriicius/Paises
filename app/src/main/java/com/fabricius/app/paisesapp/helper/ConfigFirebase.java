package com.fabricius.app.paisesapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth authreference;
    private static DatabaseReference databasereference;

    public static FirebaseAuth getAuthreference(){
        if(authreference == null){
            authreference = FirebaseAuth.getInstance();
        }
        return authreference;
    }

    public static DatabaseReference getDatabasereference(){
        if(databasereference == null){
            databasereference = FirebaseDatabase.getInstance().getReference();
        }
        return databasereference;
    }
}
