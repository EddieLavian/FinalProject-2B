package com.example.talyeh3.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeamDetails extends AppCompatActivity {
    TextView tvName;
    FirebaseDatabase database;
    DatabaseReference teamRef;
    Team t;
    String key;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);//try commit


        database = FirebaseDatabase.getInstance();
        tvName = (TextView) findViewById(R.id.tvName);
        Intent intent = getIntent();

        key = intent.getExtras().getString("keyteam");
        teamRef = database.getReference("Teams/" + key);
        Toast.makeText(TeamDetails.this, "user: " + key, Toast.LENGTH_LONG).show();
        this.retrieveData();
    }



    public void retrieveData()
    {
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                t = dataSnapshot.getValue(Team.class);
                tvName.setText("team name: " + t.name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void onClick(View v) {


    }



}
