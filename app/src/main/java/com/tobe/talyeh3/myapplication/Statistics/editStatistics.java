package com.tobe.talyeh3.myapplication.Statistics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tobe.talyeh3.myapplication.R;
import com.tobe.talyeh3.myapplication.Team.TeamDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editStatistics extends AppCompatActivity  implements View.OnClickListener{
    TextView tvName;
    EditText etGames,etGoals,etAssists, etWins;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference statisticRef;
    String key;
    Statistics s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_statistics );

        database = FirebaseDatabase.getInstance();
        tvName =(TextView)findViewById( R.id.tvName );
        etGames = (EditText) findViewById(R.id.etGames);
        etGoals = (EditText) findViewById(R.id.etGoals);
        etAssists = (EditText) findViewById(R.id.etAssists);
        etWins = (EditText) findViewById(R.id.etWins);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();
        key = intent.getExtras().getString("keyStatistic");
        statisticRef = database.getReference("Statistics/" + key);
        //Toast.makeText(EditPostActivity.this, "user: " + postRef, Toast.LENGTH_LONG).show();
        this.retrieveData();

    }

    public void retrieveData()
    {
        statisticRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    s = dataSnapshot.getValue( Statistics.class );
                    tvName.setText( s.name );
                    etGames.setText( String.valueOf( s.games ) );
                    etGoals.setText( String.valueOf( s.goals ) );
                    etAssists.setText( String.valueOf( s.assist ) );
                    etWins.setText(String.valueOf(s.wins));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View v) {

        if (v==btnSave)
        {
            if(Integer.valueOf(etGames.getText().toString())==0 &&
                    (Integer.valueOf(  etAssists.getText().toString()) > 0  || Integer.valueOf(  etGoals.getText().toString()) > 0
                            || Integer.valueOf( etWins.getText().toString()) > 0))
            {
                Toast.makeText(this, "Wrong input. The number of games can't be zero",Toast.LENGTH_SHORT).show();
            }
            else if(Integer.valueOf(etGames.getText().toString()) < Integer.valueOf( etWins.getText().toString()))
            {
                Toast.makeText(this, "Wrong input. The number of games can't be less than number of wins",Toast.LENGTH_SHORT).show();
            }
            else
            {
                statisticRef = database.getReference("Statistics/" + s.key);
                s.assist = Integer.valueOf(  etAssists.getText().toString());
                s.goals = Integer.valueOf(  etGoals.getText().toString());
                s.games = Integer.valueOf(  etGames.getText().toString());
                s.wins = Integer.valueOf( etWins.getText().toString());
                statisticRef.setValue(s);
                finish();
            }

        }




    }
}
