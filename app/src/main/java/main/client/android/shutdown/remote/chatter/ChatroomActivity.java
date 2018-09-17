package main.client.android.shutdown.remote.chatter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatroomActivity extends AppCompatActivity {
    String username;
    String chatRoomName;
    String tmpKey;
    private DatabaseReference databaseReference;

    TextView txtIspis;
    EditText txtMessage;
    Button btnSendMessage;

    String chat;
    private String usernamed;
    private String poruka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        txtIspis = findViewById(R.id.txtIspis);
        txtMessage = findViewById(R.id.txtMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        Intent fromMainActivity = getIntent();
        username = fromMainActivity.getExtras().get("username").toString();
        chatRoomName = fromMainActivity.getExtras().get("room").toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(chatRoomName);

        setTitle("Room : " + chatRoomName);

        //change listener
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append(dataSnapshot);
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

    public void sendMessage(View view){
        Map<String, Object> mapa = new HashMap<>();
        tmpKey = databaseReference.push().getKey();
        databaseReference.updateChildren(mapa);

        DatabaseReference message_root = databaseReference.child(tmpKey);
        Map<String, Object> upisPoruke = new HashMap<>();
        upisPoruke.put("name", username);
        upisPoruke.put("message", txtMessage.getText().toString());

        message_root.updateChildren(upisPoruke);
        txtMessage.setText("");
    }

    private void append(DataSnapshot dataSnapshot){


        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            poruka = (String)((DataSnapshot)iterator.next()).getValue();
            usernamed = (String)((DataSnapshot)iterator.next()).getValue();

            chat =  usernamed + ": " + poruka + "\n";
        }

        txtIspis.append(chat);
    }
}
