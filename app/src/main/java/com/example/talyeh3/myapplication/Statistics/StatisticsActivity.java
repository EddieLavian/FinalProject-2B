package com.example.talyeh3.myapplication.Statistics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class StatisticsActivity extends AppCompatActivity{
    ListView lv;
    int i = 0;
    String keyStatistic="";
    ArrayList<Statistics> statistics;
    ArrayList<String> myStatistics;
    StatisticsAdapter statisticsAdapter;
    private DatabaseReference database,statisticsDatabase;
    String keyTeam="";
    ProgressDialog progressDialog;
    Button btnSortGames,btnSortAssists,btnSortGolas;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_statistics );
/*
        btnSortGames=(Button) findViewById( R.id.btnSortGames );
        btnSortAssists=(Button)findViewById( R.id.btnSortAssists );
        btnSortGolas=(Button) findViewById( R.id.btnSortGolas );
        btnSortGames.setOnClickListener( this );
        btnSortAssists.setOnClickListener( this );
        btnSortGolas.setOnClickListener( this );
*/
        Intent intent = getIntent();
        keyTeam = intent.getExtras().getString("teamKey");

        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Teams/"+keyTeam+"/statistics");
        lv = (ListView) findViewById( R.id.lv);
        if(database!=null)
        {
            this.retriveData();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Statistics s = statistics.get(position);
                Intent intent = new Intent(StatisticsActivity.this, editStatistics.class);
                intent.putExtra("keyStatistic", s.key );
                startActivity(intent);


            }





        });

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
                    Toast.makeText(StatisticsActivity.this, i+keyStatistic, Toast.LENGTH_LONG).show();
                    statisticsDatabase = FirebaseDatabase.getInstance().getReference("Statistics/" + keyStatistic);

                    ValueEventListener valueEventListener = statisticsDatabase.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(DataSnapshot snapshot) {
                                Statistics s = snapshot.getValue( Statistics.class );
                                for (int j =0; j<statistics.size();j++)//for not duplicate on the screen
                                {
                                    if(statistics.get( j ).name.equals( s.name ))
                                    {
                                        statistics.remove( j );
                                    }
                                }


                                statistics.add( s );
                            Collections.sort(statistics, new Comparator<Statistics>(){
                                public int compare(Statistics obj1, Statistics obj2)
                                {
                                    // TODO Auto-generated method stub
                                    return (obj1.goals > obj2.goals) ? -1: (obj1.goals > obj2.goals) ? 1:0 ;
                                }
                            });


                            Log.d( "onStart", snapshot.toString() );

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



/*
    public void onClick(View v) {
        if (v==btnSortAssists)
        {
            Collections.sort(statistics, new Comparator<Statistics>(){
                public int compare(Statistics obj1, Statistics obj2)
                {
                    // TODO Auto-generated method stub
                    return (obj1.assist > obj2.assist) ? -1: (obj1.assist > obj2.assist) ? 1:0 ;
                }
            });
        }
        if(v==btnSortGames)
        {
            Collections.sort(statistics, new Comparator<Statistics>(){
                public int compare(Statistics obj1, Statistics obj2)
                {
                    // TODO Auto-generated method stub
                    return (obj1.games > obj2.games) ? -1: (obj1.games > obj2.games) ? 1:0 ;
                }
            });
            finish();
            startActivity(getIntent());
        }
        if (v==btnSortGolas)
        {
            Collections.sort(statistics, new Comparator<Statistics>(){
                public int compare(Statistics obj1, Statistics obj2)
                {
                    // TODO Auto-generated method stub
                    return (obj1.goals > obj2.goals) ? -1: (obj1.goals > obj2.goals) ? 1:0 ;
                }
            });

        }
        finish();
        startActivity(getIntent());

    }

*/

    }
