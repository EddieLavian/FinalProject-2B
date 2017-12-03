package com.example.talyeh3.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {
    TextView tvUserName,tvAge,tvTeam;
    FirebaseDatabase database;
    DatabaseReference userRef;
    String key;
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        database = FirebaseDatabase.getInstance();
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTeam = (TextView) findViewById(R.id.tvTeam);
        tvAge = (TextView) findViewById(R.id.tvAge);
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");
        userRef = database.getReference("Users/" + key);
        this.retrieveData();
    }

    public void retrieveData()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                tvUserName.setText(u.userName);
                tvAge.setText(String.valueOf(u.age));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
