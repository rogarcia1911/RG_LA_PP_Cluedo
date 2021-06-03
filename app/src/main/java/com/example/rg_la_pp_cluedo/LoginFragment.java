package com.example.rg_la_pp_cluedo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {


    EditText editText;
    EditText password;
    Button button;
    User user;


    FirebaseDatabase database;
    DatabaseReference playerRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editText = view.findViewById(R.id.etUserName);
        password = view.findViewById(R.id.etPassword);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        user = new User();
        //getView().findViewById(R.id.etUserName);
        //button = getView().findViewById(R.id.etPassword);

        //database=FirebaseDatabase.getInstance();
        String pref = getString(R.string.PREFapp);
        // miramos si existe el usuario
        //SharedPreferences preferences = this.getActivity().getSharedPreferences(pref,0);
        //user.setName(preferences.getString("Users",""));
        if(user != null && user.getName() != null && !user.getName().equals("")){
              playerRef=database.getReference("Users/"+user.getName());
              addEventListener();
              playerRef.setValue("");
        }
    }

    private void logIn(View view) {
        user.setName(editText.getText().toString());
        editText.setText("");
        if(!user.getName().equals("")){
            button.setText("Logging in");
        };
    }

    private void addEventListener() {
        final Context context=getContext();
        final SharedPreferences preferences=this.getActivity().getSharedPreferences("PREFS",0);
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!user.getName().equals("")){
                  SharedPreferences.Editor editor=preferences.edit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    button.setText("LOG IN");
                    button.setEnabled(true);
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }


}