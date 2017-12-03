package com.example.talyeh3.myapplication;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyTeams extends AppCompatActivity {
    ListView lv;

    int i = 1;
    String keyteam="";
    ArrayList<Team> teams;
    ArrayList<String> myTeams;
    MyTeamsAdapter allTeamsAdapter;
    private DatabaseReference database,teamDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//
    String myUserId = user.getUid();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);


        database = FirebaseDatabase.getInstance().getReference("Users/"+myUserId+"/teams");
        lv = (ListView) findViewById(R.id.lv);
        this.retriveData();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team t = teams.get(position);
                Intent intent = new Intent(MyTeams.this, TeamDetails.class);
                intent.putExtra("keyteam", keyteam );
                startActivity(intent);


            }





        });






    }
    public void retriveData() {
       database.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                myTeams = new ArrayList<String>();
                teams = new ArrayList<Team>();
               for (DataSnapshot data : snapshot.getChildren()) {
                        keyteam = (String) snapshot.child(String.valueOf(i)).getValue();
                        teamDatabase = FirebaseDatabase.getInstance().getReference("Teams/" + keyteam);


                   ValueEventListener valueEventListener = teamDatabase.addValueEventListener(new ValueEventListener() {
                       public void onDataChange(DataSnapshot snapshot) {
                           Team t = snapshot.getValue(Team.class);
                           Toast.makeText(MyTeams.this, "user: " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                           teams.add(t);
                       }
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

                   i++;

                }
                allTeamsAdapter = new MyTeamsAdapter(MyTeams.this, 0, 0, teams);
                lv.setAdapter(allTeamsAdapter);
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
