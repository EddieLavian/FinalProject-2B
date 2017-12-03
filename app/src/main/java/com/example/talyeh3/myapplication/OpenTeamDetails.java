package com.example.talyeh3.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OpenTeamDetails extends AppCompatActivity implements View.OnClickListener {

    EditText etTeamName;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference teamRef,userRef,userRef2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myUserId = user.getUid();
    ArrayList<String> myTeams;
    User u ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_team_details);
        database = FirebaseDatabase.getInstance();
        etTeamName = (EditText) findViewById(R.id.etTeamName);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        userRef = database.getReference("Users/" + myUserId);
        this.retriveData();
    }

    private void retriveData() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().toString();
        List<String> players;
        players = new ArrayList<String>();
        players.add(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
        Team t = new Team(uid,etTeamName.getText().toString(),players,"");
        teamRef = database.getReference("Teams").push();
        t.key = teamRef.getKey();
        teamRef.setValue(t);
        myTeams = new ArrayList<String>();
        userRef = database.getReference("Users/" + myUserId);
        userRef2 = database.getReference("Users/" + myUserId+"/teams/0");

       // Toast.makeText(OpenTeamDetails.this,  userRef2.toString(), Toast.LENGTH_LONG).show();
        u.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        u.teams.add(t.key);
        userRef.setValue(u);
       if(userRef2!=null)
       {
           userRef2.removeValue();
       }
        finish();
        Intent intent = new Intent(OpenTeamDetails.this,OpenTeam.class);
        intent.putExtra( "teamKey",t.key);
        startActivity(intent);
    }
}
