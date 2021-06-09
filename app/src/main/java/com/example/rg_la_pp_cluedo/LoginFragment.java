package com.example.rg_la_pp_cluedo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Icon;
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    LinearLayout llvDataUser;
    ConstraintLayout clSignIn;
    ImageView ivIcon;
    TextView UserName, tvDataLabel,tvDataContent;
    EditText etEmail, etPassword;
    Button btLogIn, btSignIn, btLogOut;
    User player;
    Icon icon;

    SharedPreferences shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, userPlayerRef, iconPlayerRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        shPreferences = this.getActivity().getSharedPreferences(getString(R.string.PREFapp),0);
        database = DataBaseConnection.getFirebase();
        mAuth = FirebaseAuth.getInstance();

        //TODO: preferancias idioma y sonido
        shPreferences.getString("appLanguage","");
        shPreferences.getBoolean("appSound",true);

        UserName = getView().findViewById(R.id.UserName);
        llvDataUser = getView().findViewById(R.id.llvDataUser);
        clSignIn = getView().findViewById(R.id.clSignIn);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            llvDataUser.setVisibility(View.VISIBLE);
            tvDataLabel = getView().findViewById(R.id.tvDataLabel);
            tvDataContent = getView().findViewById(R.id.tvDataContent);
            btLogOut = getView().findViewById(R.id.btLogOut);
            chargeUserData();
            btLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences.Editor editor = shPreferences.edit();
                    editor.putString("userName", "");
                    editor.apply();

                    llvDataUser.setVisibility(View.GONE);
                    clSignIn.setVisibility(View.VISIBLE);
                }
            });
            Toast.makeText(getContext(), "Sesión ya iniciada", Toast.LENGTH_SHORT).show();
        } else {
            clSignIn.setVisibility(View.VISIBLE);
            etEmail = getView().findViewById(R.id.etEmail);
            etPassword = getView().findViewById(R.id.etPassword);
            btLogIn = getView().findViewById(R.id.btLogIn);
            btSignIn = getView().findViewById(R.id.btSignIn);

            btLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Iniciando sesión
                    logIn();
                    etPassword.setText("");

                    btLogIn.setText("LOG IN");
                    btLogIn.setEnabled(true);
                }
            });

            btSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSignIn();

                    btSignIn.setText("SIGN IN");
                    btSignIn.setEnabled(true);
                }
            });
        }
    }

    private void addEventListener() {
        // Leer de la base de datos
        userPlayerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Incia sesión - almacenasmo el usuario en el sharedPreferences
                player = dataSnapshot.getValue(User.class);
                if(!player.getName().equals("")){
                  SharedPreferences.Editor editor = shPreferences.edit();
                  editor.putString("userName", player.getName());
                  editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ERROR al iniciar sesión
                // TODO : Texto para traducir
                Toast.makeText(getContext(),"Error al iniciar sesión",Toast.LENGTH_SHORT).show();
            }
        });

        iconPlayerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                icon = snapshot.getValue(Icon.class);
                if(icon != null && icon.isSelected()){
                    SharedPreferences.Editor editor = shPreferences.edit();
                    editor.putInt("userImage", icon.getImage());
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Error al recuperar el icono",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void logIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (Validate(email,password)) {
            btLogIn.setText("Logging in");
            btLogIn.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    setPlayer(email);

                                    /*SharedPreferences.Editor editor = shPreferences.edit();
                                    editor.putString("userName", player.getName());
                                    editor.apply();*/

                    etEmail.setText("");
                    chargeUserData();

                    clSignIn.setVisibility(View.GONE);
                    llvDataUser.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Sesión iniciada", Toast.LENGTH_SHORT).show();
                    //updateUI(user); Actualizar la Interfaz de Usuario
                } else {
                    // Error en la autentificación ya sea por que no está registrado o por poner mal algún dato
                    // TODO: Traducir textos de error
                    String tag = "LogIn Authentication failed.";
                    if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidCredentialsException.class) {
                        Toast.makeText(getContext(), tag + " El email no es válido.", Toast.LENGTH_SHORT).show();
                    } if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidUserException.class) {
                        Toast.makeText(getContext(), tag + " No exite un usuario con ese email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), tag, Toast.LENGTH_SHORT).show();
                    }
                    Log.i("LogIn", String.valueOf(task.getException()));
                    //updateUI(null);
                }
            });
            Toast.makeText(getContext(),"Validado",Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getContext(),"No se ha podido iniciar sesión",Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogSignIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signin, null);
        // LinearLayout donde se irán agregando las cartas incorrectas
        final EditText dsgUserame = view.findViewById(R.id.dsgUsername);
        final EditText dsgEmail = view.findViewById(R.id.dsgEmail);
        final EditText dsgPassword = view.findViewById(R.id.dsgPassword);

        builder.setView(view)
                .setPositiveButton("R.string.signin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        String userName = dsgUserame.getText().toString();
                        String email = dsgEmail.getText().toString();
                        String password = dsgPassword.getText().toString();
                        if (ValidateSignIn(userName,email,password)) {
                            btSignIn.setText("Signing in");
                            btSignIn.setEnabled(false);
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        newPlayer(userName, email);

                                        /*SharedPreferences.Editor editor = shPreferences.edit();
                                        editor.putString("userName", player.getName());
                                        editor.apply();*/

                                        chargeUserData();

                                        clSignIn.setVisibility(View.GONE);
                                        llvDataUser.setVisibility(View.VISIBLE);
                                        Toast.makeText(getContext(), "Usuario creado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String tag = "SignIn Authentication failed.";
                                        if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidCredentialsException.class) {
                                            Toast.makeText(getContext(), tag + " El email no es válido.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), tag, Toast.LENGTH_SHORT).show();
                                        }
                                        Log.i("SignIn", String.valueOf(task.getException()));
                                        //updateUI(null);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(),"No se ha podido registrar",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("R.string.cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void newPlayer(String userName, String email) {
        player = new User();
        player.setName(userName);
        player.setEmail(email);
        icon = new Icon(true,R.drawable.personaje_amapola);

        userPlayerRef = database.getDatabase().getReference("Players/"+userName+"/User");
        addEventListener();

        userPlayerRef.setValue(player);
        iconPlayerRef.setValue(icon);
    }

    private void chargeUserData() {
        //TODO: traducir textos
        ivIcon.setImageResource(icon.getImage());
        UserName.setText(player.getName());
        tvDataLabel.setText("Puntos\n" +
                            "Partidas Solo\n" +
                            "Partidas Multijugador");
        tvDataContent.setText(player.getPoints() + "\n" +
                            player.getNumSoloMatchs() + "\n" +
                            player.getNumMultiMatchs());

    }

    private void setPlayer(String email){
        userPlayerRef = database.child("Players").child(email);

    }

    /**
     * Comprobación para Log In
     * @param email
     * @param password
     * @return
     */
    private boolean Validate(String email, String password) {
        if (email.equals("") || password.equals("")) {
            Toast.makeText(getContext(), "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Comprovacion para Sign In
     * @param email
     * @param password
     * @param name
     * @return
     */
    private boolean ValidateSignIn(String email, String password, String name) {
        if (name.equals("")) {
            Toast.makeText(getContext(), "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return Validate(email, password);
    }
}