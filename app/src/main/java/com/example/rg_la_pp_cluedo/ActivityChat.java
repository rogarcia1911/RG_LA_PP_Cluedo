package com.example.rg_la_pp_cluedo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rg_la_pp_cluedo.BBDD.ChatMessage;
import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class ActivityChat extends AppCompatActivity {

    private TextView tvName;
    private EditText etMessage;
    private Button btnSendMessage;
    private RecyclerView rvMessages;

    private RecieveMessage message;
    private MessageAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    SharedPreferences shSettings;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tvName = findViewById(R.id.tvName);
        etMessage = findViewById(R.id.etMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        rvMessages = findViewById(R.id.rvMessages);

        adapter = new MessageAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(manager);
        rvMessages.setAdapter(adapter);

        shSettings = getSharedPreferences(getString(R.string.PREFsetttings), 0);
        userName = shSettings.getString("userName","");

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniFirebaseChat().push().setValue(new SendMessage(userName, etMessage.getText().toString(), ServerValue.TIMESTAMP));
                etMessage.setText("");
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();
            }
        });

        iniFirebaseChat().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                message = dataSnapshot.getValue(RecieveMessage.class);
                adapter.addMessage(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setScrollBar(){
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    private DatabaseReference iniFirebaseChat(){
        DatabaseReference database = DataBaseConnection.getFirebase();
        String roomName = getIntent().getStringExtra("roomName");
        tvName.setText(roomName+"Chat");
        databaseReference = database.getDatabase().getReference("Rooms/"+roomName+"/chat");

        return databaseReference;
    }
}