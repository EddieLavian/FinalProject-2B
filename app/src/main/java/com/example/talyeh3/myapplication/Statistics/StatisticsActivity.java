package com.example.talyeh3.myapplication.Statistics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.CreateGame.CreateGame;
import com.example.talyeh3.myapplication.MyTeams;
import com.example.talyeh3.myapplication.MyTeamsAdapter;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Team;
import com.example.talyeh3.myapplication.TeamDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    ListView lv;
    int i = 0;
    String keyStatistic="";
    ArrayList<Statistics> statistics;
    ArrayList<String> myStatistics;
    StatisticsAdapter statisticsAdapter;
    private DatabaseReference database,statisticsDatabase;
    String keyTeam="";
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //String myUserId = user.getUid();
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_statistics );


        Intent intent = getIntent();
        keyTeam = intent.getExtras().getString("teamKey");
        Toast.makeText(StatisticsActivity.this, "hghg         "+keyTeam, Toast.LENGTH_LONG).show();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Teams/"+keyTeam+"/statistics");
        lv = (ListView) findViewById( R.id.lv);
        if(database!=null)
        {
            this.retriveData();
        }
        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team t = teams.get(position);
                Intent intent = new Intent(MyTeams.this, TeamDetails.class);
                intent.putExtra("keyteam", t.key );
                startActivity(intent);


            }





        });
        */
    }





    public void retriveData() {
        progressDialog.setMessage("load Please Wait...");
        progressDialog.show();

        database.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {
                myStatistics = new ArrayList<String>();
                statistics = new ArrayList<Statistics>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("onDataChange", data.getValue().toString());
                    keyStatistic = (String) snapshot.child(String.valueOf(i)).getValue();

                    statisticsDatabase = FirebaseDatabase.getInstance().getReference("Statistics/" + keyStatistic);
                    ValueEventListener valueEventListener = statisticsDatabase.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(DataSnapshot snapshot) {

                            Statistics s = snapshot.getValue(Statistics.class);
                            statistics.add(s);
                            Log.d("onStart", snapshot.toString());
                            statisticsAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }


                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                    i++;
                }
                statisticsAdapter = new StatisticsAdapter(StatisticsActivity.this, 0, 0, statistics);
                lv.setAdapter(statisticsAdapter);
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
