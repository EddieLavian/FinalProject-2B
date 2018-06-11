package com.tobe.talyeh3.myapplication.Team;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tobe.talyeh3.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    ProgressDialog progressDialog;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_my_teams);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Users/"+myUserId+"/teams");
        lv = (ListView) findViewById( R.id.lv);
        if(database!=null)
        {
            this.retriveData();
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team t = teams.get(position);
                Intent intent = new Intent(MyTeams.this, TeamDetails.class);
                intent.putExtra("keyteam", t.key );
                startActivity(intent);


            }





        });


    }


    public void retriveData() {
        progressDialog.setMessage("load Please Wait...");
        progressDialog.show();
       database.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {
                myTeams = new ArrayList<String>();
                teams = new ArrayList<Team>();
               for (DataSnapshot data : snapshot.getChildren()) {
                         Log.d("onDataChange", data.getValue().toString());
                        keyteam = (String) snapshot.child(String.valueOf(i)).getValue();
                        if (keyteam==null)
                        {
                            //Toast.makeText(MyTeams.this, "You don't have teams yettttttttttttttttttt", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            return;
                        }
                        teamDatabase = FirebaseDatabase.getInstance().getReference("Teams/" + keyteam);
                  ValueEventListener valueEventListener = teamDatabase.addValueEventListener(new ValueEventListener() {

                       public void onDataChange(DataSnapshot snapshot) {

                           Team t = snapshot.getValue(Team.class);
                           if (keyteam==null||t==null)
                           {
                               //Toast.makeText(MyTeams.this, "You don't have teams yetrrrrrrrrrrrrrrrrrrrrrrr", Toast.LENGTH_LONG).show();
                               progressDialog.dismiss();
                               return;
                           }

                           for (int j = 0; j<teams.size();j++)
                           {
                               if (t.key.equals(teams.get(j).key))
                                   teams.remove(j);
                           }

                           teams.add(t);
                           Log.d("onStart", snapshot.toString());
                           allTeamsAdapter.notifyDataSetChanged();
                           progressDialog.dismiss();
                       }


                       public void onCancelled(DatabaseError databaseError) {
                           if (keyteam==null)
                           {
                               Toast.makeText(MyTeams.this, "You don't have teams yetppppppppppppppppp", Toast.LENGTH_LONG).show();
                               progressDialog.dismiss();
                               return;
                           }
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
