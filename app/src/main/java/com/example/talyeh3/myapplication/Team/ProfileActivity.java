package com.example.talyeh3.myapplication.Team;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.ProfilePage;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Statistics.Statistics;
import com.example.talyeh3.myapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvUserName,tvAge,tvCity;
    TextView btnSave;
    FirebaseDatabase database;
    DatabaseReference userRef,userRef2,userRefTeam;
    String key,teamKey;
    User u;
    Team t;
    String delete;

    String photo;
    ImageView imgProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        tvUserName = (TextView) findViewById(R.id.tvUserName);

        tvCity = (TextView) findViewById(R.id.tvCity);


        tvAge = (TextView) findViewById(R.id.tvAge);
        btnSave = (TextView) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();
        photo = intent.getExtras().getString("photo");
        delete=intent.getExtras().getString("delete");
       // Toast.makeText(ProfileActivity.this,  delete, Toast.LENGTH_LONG).show();
        imgProfile = (ImageView)findViewById( R.id.imgProfile);
        Picasso
                .with( ProfileActivity.this )
                .load( photo)
                .fit() // will explain later
                .into( imgProfile );

        teamKey = intent.getExtras().getString("team");
        key = intent.getExtras().getString("key");
        userRef = database.getReference("Users/" + key);
        userRefTeam = database.getReference("Teams/" + teamKey);
        userRef2 = database.getReference( "Users/" + key + "/teams/0" );
        this.retrieveData();
    }


    public void retrieveData()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                tvUserName.setText(u.userName);
                tvCity.setText( u.city );
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void onClick(View v) {


        t.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (delete!=null)//delete player from the team
        {
            t.users.remove( u.uid );
            u.teams.remove(t.key);
            t.statistics.remove(key+t.key);
            String keyStatistics=key+t.key;

            DatabaseReference statisticsPlayer = FirebaseDatabase.getInstance().getReference().getRoot().child("Statistics/"+keyStatistics);//remove Statistics player from team
            statisticsPlayer.setValue(null);
        }
        else if (delete== null )//add player to the team
        {
            t.users.add(u.uid);
            u.teams.add(t.key);
            t.statistics.add(key+t.key);
            String keyStatistics=key+t.key;
            Statistics s=new Statistics( keyStatistics,t.key,u.userName,0,0,0,0);
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Statistics").child(keyStatistics).setValue(s);
        }

        userRefTeam.setValue(t);
        userRef.setValue(u);
        if (userRef2!=null)
        {
            if (userRef2.getKey().equals( "0" ))
                userRef2.removeValue();
        }

        finish();
    }



}