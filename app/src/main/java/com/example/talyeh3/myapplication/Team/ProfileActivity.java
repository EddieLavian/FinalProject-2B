package com.example.talyeh3.myapplication.Team;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Statistics.Statistics;
import com.example.talyeh3.myapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvUserName,tvAge,tvTeam;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference userRefTeam;
    String key,teamKey;
    User u;
    Team t;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        tvUserName = (TextView) findViewById(R.id.tvUserName);

        tvTeam = (TextView) findViewById(R.id.tvTeam);


        tvAge = (TextView) findViewById(R.id.tvAge);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();

        teamKey = intent.getExtras().getString("team");
        key = intent.getExtras().getString("key");
        userRef = database.getReference("Users/" + key);
        userRefTeam = database.getReference("Teams/" + teamKey);
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

        userRefTeam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                t = dataSnapshot.getValue(Team.class);

                tvTeam.setText("Team Name "+ t.name);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onClick(View v) {
        userRefTeam = database.getReference("Teams/" + t.key);
        t.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        t.users.add(u.uid);
        String myUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String myUserMail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        t.statistics.add(key+t.key);



        userRefTeam.setValue(t);
        userRef = database.getReference("Users/" + key);
        u.teams.add(t.key);

        userRef.setValue(u);
        String keyStatistics=key+t.key;
        Statistics s=new Statistics( keyStatistics,t.key,u.userName,0,0,0);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Statistics").child(keyStatistics).setValue(s);



        finish();
    }



}