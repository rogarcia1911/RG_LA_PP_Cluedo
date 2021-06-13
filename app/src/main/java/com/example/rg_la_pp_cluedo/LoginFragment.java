package com.example.rg_la_pp_cluedo;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    LinearLayout llvDataUser;
    ConstraintLayout clSignIn;
    ImageView ivAvatar;
    TextView UserName, tvDataLabel,tvDataContent;
    EditText etUserName, etPassword;
    Button btLogIn, btSignIn, btLogOut;
    private Spinner idiomSpinner;

    SharedPreferences shSettings,shPreferences;
    FirebaseAuth mAuth;
    DatabaseReference database, userDataRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        shSettings = this.getActivity().getSharedPreferences(getString(R.string.PREFsetttings), 0);
        shPreferences = this.getActivity().getSharedPreferences(getString(R.string.PREFapp),0);
        database = DataBaseConnection.getFirebase();
        mAuth = FirebaseAuth.getInstance();


        ivAvatar = getView().findViewById(R.id.ivAvatar);
        UserName = getView().findViewById(R.id.UserName);
        llvDataUser = getView().findViewById(R.id.llvDataUser);
        clSignIn = getView().findViewById(R.id.clSignIn);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            ViewDataUser();
            //Ningún userName puede tener @
            userDataRef = database.getDatabase().getReference("Users/"+shSettings.getString("userName","@")+"/User");
            addEventListener();
            userDataRef.get();
            UserName.setText(R.string.txt_recuperando);
        } else {
            ViewSingIn();
        }
    }

    private void ViewSingIn() {
        llvDataUser.setVisibility(View.GONE);
        clSignIn.setVisibility(View.VISIBLE);
        etUserName = getView().findViewById(R.id.etUserName);
        etPassword = getView().findViewById(R.id.etPassword);
        btLogIn = getView().findViewById(R.id.btLogIn);
        btSignIn = getView().findViewById(R.id.btSignIn);

        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciando sesión
                logIn();
                etPassword.setText("");
                btLogIn.setText(R.string.txt_login);
                btLogIn.setEnabled(true);
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSignIn();
                btSignIn.setText(R.string.txt_sign_in);
                btSignIn.setEnabled(true);
            }
        });
    }

    private void ViewDataUser() {
        clSignIn.setVisibility(View.GONE);
        llvDataUser.setVisibility(View.VISIBLE);
        tvDataLabel = getView().findViewById(R.id.tvDataLabel);
        tvDataContent = getView().findViewById(R.id.tvDataContent);
        btLogOut = getView().findViewById(R.id.btLogOut);
        btLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editorSettings = shSettings.edit();
            editorSettings.remove("userName");
            editorSettings.remove("userImage");
            editorSettings.apply();

            SharedPreferences.Editor editor = shPreferences.edit();
            editor.clear();
            editor.apply();

            SharedPreferences.Editor editorGameMulti = getActivity().getSharedPreferences( getString(R.string.PREFmultiGame), Context.MODE_PRIVATE).edit();
            editorGameMulti.clear();
            editorGameMulti.apply();

            chargeUserData(null);
            ViewSingIn();
        });
    }

    private void addEventListener() {
        // Leer de la base de datos
        userDataRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Incia sesión - almacenasmo el usuario en el sharedPreferences
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    SharedPreferences.Editor edit = shSettings.edit();
                    edit.putString("userName",user.getName());
                    edit.putInt("userImage", user.getAvatar());
                    edit.apply();
                    SharedPreferences.Editor editor = shPreferences.edit();
                    editor.putString("userData", user.getEmail() + "\n" +
                                                user.getPoints() + "\n" +
                                                user.getNumSoloMatchs() + "\n" +
                                                user.getNumMultiMatchs());
                    editor.apply();
                    chargeUserData(user);
                } else {
                    SharedPreferences.Editor edit = shSettings.edit();
                    edit.putString("userName","");
                    edit.putInt("userImage",R.drawable.personaje_amapola);
                    edit.apply();
                    Toast.makeText(getContext(),R.string.err_inico_Sesion,Toast.LENGTH_SHORT).show();
                    ViewSingIn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ERROR al iniciar sesión
                Toast.makeText(getContext(),R.string.err_error_sesion,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logIn() {
        String tag = "LogIn";
        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (Validate(userName,password)) {
            // recuperamos el email de la base de datos
            Task<DataSnapshot> email = database.child("Users").child(userName).child("User").child("email").get();
            email.addOnCompleteListener(taskEmail -> {
                btLogIn.setText(R.string.txt_sign_in);
                btLogIn.setEnabled(false);
                String sEmail = taskEmail.getResult().getValue(userName.getClass());
                if (sEmail != null && !sEmail.isEmpty())
                    mAuth.signInWithEmailAndPassword(sEmail, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //LogIn successful
                            userDataRef = database.getDatabase().getReference("Users/"+userName+"/User");
                            addEventListener();
                            userDataRef.get();

                            etUserName.setText("");
                            ViewDataUser();
                            Toast.makeText(getContext(), R.string.txt_sesion_inicio, Toast.LENGTH_SHORT).show();
                            //updateUI(user); Actualizar la Interfaz de Usuario
                        } else {
                            // Error en la autentificación ya sea por que no está registrado o por poner mal algún dato
                            if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidCredentialsException.class) {
                                Toast.makeText(getContext(), tag + R.string.err_email, Toast.LENGTH_SHORT).show();
                            } if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidUserException.class) {
                                Toast.makeText(getContext(), tag + R.string.err_noExisteEmail, Toast.LENGTH_SHORT).show();
                            } if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidCredentialsException.class){
                                Toast.makeText(getContext(), tag +  R.string.err_contraseña, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), tag, Toast.LENGTH_SHORT).show();
                            }
                            Log.i("LogIn", String.valueOf(task.getException()));
                            //updateUI(null);
                        }
                    });
                else
                    Toast.makeText(getContext(), tag + " No exite ese nombre de usuario.", Toast.LENGTH_SHORT).show();

                btLogIn.setText(R.string.txt_login);
                btLogIn.setEnabled(true);
            });
        }
    }

    private void dialogSignIn() {
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signin, null);
        // LinearLayout donde se irán agregando las cartas incorrectas
        final EditText dsgUserame = view.findViewById(R.id.dsgUsername);
        final EditText dsgEmail = view.findViewById(R.id.dsgEmail);
        final EditText dsgPassword = view.findViewById(R.id.dsgPassword);

        builder.setView(view)
                .setPositiveButton(R.string.txt_sign_in, (dialog, which) -> {
                    String tag = "SignIn.";
                    // sign in the user ...
                    String userName = dsgUserame.getText().toString();
                    String email = dsgEmail.getText().toString();
                    String password = dsgPassword.getText().toString();
                    if (userName.contains("@")){
                        Toast.makeText(getContext(), R.string.err_validadoEmail, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ValidateSignIn(userName,email,password)) {
                        // recuperamos el email de la base de datos
                        Task<DataSnapshot> user = database.child("Users").child(userName).child("User").get();
                        user.addOnCompleteListener(taskUser -> {
                            btSignIn.setText(R.string.txt_sign_in);
                            btSignIn.setEnabled(false);
                            User userTask = taskUser.getResult().getValue(User.class);
                            if (userTask == null)
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        newPlayer(userName, email);

                                        ViewDataUser();

                                        Toast.makeText(getContext(), R.string.txt_usuario_ok, Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthInvalidCredentialsException.class) {
                                            Toast.makeText(getContext(), tag + R.string.err_email, Toast.LENGTH_LONG).show();
                                        } else if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthWeakPasswordException.class) {
                                            Toast.makeText(getContext(), tag +R.string.err_contraseña, Toast.LENGTH_LONG).show();
                                        } else if (task.getException().getClass() == com.google.firebase.auth.FirebaseAuthUserCollisionException.class) {
                                            Toast.makeText(getContext(), tag + R.string.err_email_si, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), tag, Toast.LENGTH_LONG).show();
                                        }
                                        Log.i("Sign In", String.valueOf(task.getException()));
                                        //updateUI(null);
                                    }
                                });
                            else
                                Toast.makeText(getContext(), tag + R.string.txt_validado, Toast.LENGTH_LONG).show();


                            btSignIn.setText(R.string.txt_sign_in);
                            btSignIn.setEnabled(true);
                        });
                    }
                })
                .setNegativeButton(R.string.txt_cancel, (dialog, which) -> dialog.cancel());

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void newPlayer(String userName, String email) {
        User user = new User();
        user.setName(userName);
        user.setEmail(email);
        user.setAvatar(R.drawable.personaje_amapola);

        userDataRef = database.getDatabase().getReference("Users/"+userName+"/User");
        addEventListener();

        userDataRef.setValue(user);
    }

    public void chargeUserData(User user) {

        ivAvatar.setImageResource((user!=null) ? user.getAvatar() : R.drawable.personaje_amapola);

        UserName.setText((user!=null) ? user.getName() : shSettings.getString("userName", "UserName"));
        //TODO: Traducir textos
        tvDataLabel.setText("Email\n" +
                "Puntos\n" +
                "Partidas Solo\n" +
                "Partidas Multijugador");
        tvDataContent.setText( (user!=null) ? user.getEmail() + "\n" +
                                            user.getPoints() + "\n" +
                                            user.getNumSoloMatchs() + "\n" +
                                            user.getNumMultiMatchs()
                                            : shPreferences.getString("userData", "") );

    }

    /**
     * Comprobación para Log In
     * @param userName
     * @param password
     * @return
     */
    private boolean Validate(String userName, String password) {
        if (userName.equals("") || password.equals("")) {
            Toast.makeText(getContext(), R.string.valideCampos, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), R.string.valideCampos, Toast.LENGTH_SHORT).show();
            return false;
        }
        return Validate(email, password);
    }

}