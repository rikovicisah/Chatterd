package main.client.android.shutdown.remote.chatter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    //firebase https://chatter-af8c5.firebaseio.com/

    Button btnRoom;
    EditText editText;
    ListView listRooms;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listaSoba;
    String username;

    //za pristup bazi
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRoom = findViewById(R.id.btnRoom);
        editText = findViewById(R.id.editText);
        listRooms = findViewById(R.id.listRooms);

        listaSoba = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listaSoba);

        listRooms.setAdapter(arrayAdapter);

        requestUsername();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                    listaSoba.clear();
                    listaSoba.addAll(set);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chatroom = new Intent(MainActivity.this, ChatroomActivity.class);
                chatroom.putExtra("room", ((TextView)view).getText().toString());
                chatroom.putExtra("username", username);
                startActivity(chatroom);
                finish();
            }
        });
    }

    private void requestUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name:");

        final EditText inputName = new EditText(this);
        builder.setView(inputName);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = inputName.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addChatRoom(View view){
        if(!editText.getText().toString().isEmpty() && editText != null){
            Map<String, Object> mapaSoba = new HashMap<>();
            mapaSoba.put(editText.getText().toString(), "");
            databaseReference.updateChildren(mapaSoba);
        }
    }
}
