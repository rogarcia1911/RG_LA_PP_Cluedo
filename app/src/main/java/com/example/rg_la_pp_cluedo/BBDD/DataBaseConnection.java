package com.example.rg_la_pp_cluedo.BBDD;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBaseConnection  {

    private static FirebaseDatabase firebaseObj = null;
    private static DatabaseReference databaseRefObj = null;
    private static DataBaseConnection connection = null;

    private DataBaseConnection(){

    }

    public static DataBaseConnection getInstance(){
        if(connection == null){
             return connection = new DataBaseConnection();
        }
        return connection;
    }


    public static DatabaseReference getFirebase(Context context){
        FirebaseApp.initializeApp(context);
        firebaseObj = FirebaseDatabase.getInstance();
        if ( firebaseObj == null ) {
            firebaseObj.setPersistenceEnabled(true);
        }
        databaseRefObj = firebaseObj.getReference();

        return databaseRefObj;
    }

    public static DatabaseReference getFirebase(){
        return databaseRefObj = firebaseObj.getReference();
    }

}
