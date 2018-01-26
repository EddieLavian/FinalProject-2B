package com.example.talyeh3.myapplication.CreateGame;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamGamesActivity extends AppCompatActivity {
    ListView lv;
    int i = 1;
    ArrayList<Game> games;
    ArrayList<String> myGames;
    TeamGamesAdapter TeamGamesAdapter;
    private DatabaseReference database,gameDatabase;
    ProgressDialog progressDialog;
    String keyTeam,keyGame;
    DatabaseReference attendingRef;
    FirebaseDatabase attendingDatabase;
    Game g;
    Boolean ifAttending=true,checkIfAttending=false,first=true;
    String myUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_team_games );
        Intent intent = getIntent();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myUserId = user.getDisplayName();
        keyTeam = intent.getExtras().getString("teamKey");
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Teams/"+keyTeam+"/games");
        lv = (ListView) findViewById( R.id.lv);
        if(database!=null)
        {
            this.retriveData();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkIfAttending=false;
                g = games.get(position);
                attendingDatabase = FirebaseDatabase.getInstance();
                attendingRef = attendingDatabase.getReference("Games/" + g.key);
                AlertDialog.Builder builder = new AlertDialog.Builder(TeamGamesActivity.this);
                for (int i = 0 ; i<g.whoIsComming.size();i++)
                {
                    if(g.whoIsComming.get( i ).equals( myUserId ))
                    {
                        checkIfAttending=true;
                    }
                }
                if (checkIfAttending.equals( false ))
                {
                    builder.setTitle("Attending");
                    builder.setMessage("Will You Come To This Game?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes, I Will Come", new HandleAlertDialogListener());
                    builder.setNegativeButton("Cancel", new HandleAlertDialogListener());
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
                else
                {

                    builder.setTitle("Attending");
                    builder.setMessage("Are You Sure You want cancel your attending?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new HandleAlertDialogListener());
                    builder.setNegativeButton("Cancel", new HandleAlertDialogListener());
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
            }





        });






    }
    public void retriveData() {
        progressDialog.setMessage( "load Please Wait..." );
        progressDialog.show();
        database.addValueEventListener( new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                myGames = new ArrayList<String>();
                games = new ArrayList<Game>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d( "onDataChange", data.getValue().toString() );
                    keyGame = (String) snapshot.child( String.valueOf( i ) ).getValue();
                    if (keyGame==null)
                    {
                        Toast.makeText(TeamGamesActivity.this, "you dont have planing games yet", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return;
                    }
                    gameDatabase = FirebaseDatabase.getInstance().getReference( "Games/" + keyGame );
                    ValueEventListener valueEventListener = gameDatabase.addValueEventListener( new ValueEventListener() {
                        public void onDataChange(DataSnapshot snapshot) {
                            Game g = snapshot.getValue( Game.class );
                            if(first == true)
                            {
                                games.add( g );
                                Log.d( "onStart", snapshot.toString() );

                            }
                            if (TeamGamesAdapter!=null)
                                 TeamGamesAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                        public void onCancelled(DatabaseError databaseError) {
                        }
                    } );
                    i++;
                }
                TeamGamesAdapter = new TeamGamesAdapter( TeamGamesActivity.this, 0, 0, games,ifAttending ,myUserId);
                lv.setAdapter( TeamGamesAdapter );
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }


    public  class  HandleAlertDialogListener implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            first=false;
            if (which==-1)//yes
            {
                if (checkIfAttending==false)
                {
                    checkIfAttending=true;
                    ifAttending = false;
                    attendingRef = attendingDatabase.getReference( "Games/" + g.key );
                    g.attending++;
                    g.whoIsComming.add( myUserId );
                    attendingRef.setValue( g );
                }
                else
                {
                    checkIfAttending=false;
                    ifAttending = true;
                    attendingRef = attendingDatabase.getReference( "Games/" + g.key );
                    g.attending--;
                    g.whoIsComming.remove( myUserId );
                    attendingRef.setValue( g );
                }
            }
            else if(which == -2)//no, maybe later
            {

            }
        }
    }

}



