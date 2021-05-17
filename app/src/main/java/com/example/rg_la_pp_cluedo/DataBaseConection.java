package com.example.rg_la_pp_cluedo;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class DataBaseConection  extends AppCompatActivity {

    private FirebaseDatabase firebaseObj = null;
    private DatabaseReference databaseRefObj = null;
    private static DataBaseConection connection = null;

    private DataBaseConection(){

    }

    public static DataBaseConection getInstance(){
        if(connection == null){
             return connection = new DataBaseConection();
        }
        return connection;
    }


    public DatabaseReference getFirebase(){
        FirebaseApp.initializeApp(getApplicationContext());
        firebaseObj = FirebaseDatabase.getInstance();
        firebaseObj.setPersistenceEnabled(true);
        databaseRefObj = firebaseObj.getReference();

        return databaseRefObj;
    }



}
