package com.example.talyeh3.myapplication.CreateGame;

import android.app.Dialog;
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

import com.example.talyeh3.myapplication.AllUsersAdapter;
import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamGamesActivity extends AppCompatActivity implements com.example.talyeh3.myapplication.CreateGame.TeamGamesAdapter.customButtonListener {
    ListView lv;
    int i = 1;
    ArrayList<Game> games;
    ArrayList<String> myGames;
    TeamGamesAdapter TeamGamesAdapter;
    private DatabaseReference database,gameDatabase,database2,teamDatabase;
    ProgressDialog progressDialog;
    String keyTeam,keyGame;
    DatabaseReference attendingRef;
    FirebaseDatabase attendingDatabase;
    Game g;
    Boolean ifAttending=true,checkIfAttending=false,first=true;
    String myUserId;
    Dialog dialog;

    AllUsersAdapter allPlayersAdapter;
    ArrayList<User> users;
    String keyUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_team_games );
        Intent intent = getIntent();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dialog= new Dialog(this);

        myUserId = user.getUid();
        keyTeam = intent.getExtras().getString("teamKey");
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Teams/"+keyTeam+"/games");
        lv = (ListView) findViewById( R.id.lv);
        if(database!=null)
        {
            this.retriveData();
        }
/*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkIfAttending=false;
                g = games.get(position);
                TeamGamesAdapter = new TeamGamesAdapter( TeamGamesActivity.this, 0, 0, games,ifAttending ,myUserId,dialog,g.key);
                lv.setAdapter( TeamGamesAdapter );
                attendingDatabase = FirebaseDatabase.getInstance();
                attendingRef = attendingDatabase.getReference("Games/" + g.key);
                AlertDialog.Builder builder = new AlertDialog.Builder(TeamGamesActivity.this);
                for (int i = 1 ; i<g.whoIsComming.size();i++)
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
                    AlertDialog dialogAlert=builder.create();
                    dialogAlert.show();
                }
            }


        });



*/


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
                        finish();
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
                TeamGamesAdapter = new TeamGamesAdapter( TeamGamesActivity.this, 0, 0, games,ifAttending ,myUserId,dialog,"no");
                TeamGamesAdapter.setCustomButtonListner(TeamGamesActivity.this);
                lv.setAdapter( TeamGamesAdapter );

            }
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    @Override
    public void onButtonClickListner(int position, Game value) {//who is comming button


            database2 = FirebaseDatabase.getInstance().getReference( "Games/" + value.key + "/whoIsComming" );
            // Toast.makeText(context, tvPlayers.getParent().toString(),Toast.LENGTH_SHORT).show();
            retriveDataPlayers();
            dialog.setContentView( R.layout.activity_all_users );
            dialog.setCancelable( true );
            lv = (ListView) dialog.findViewById( R.id.lv );
            dialog.show();
    }

    public void onButtonClickListner2(int position, Game value) {
        checkIfAttending=false;
        g = games.get(position);
        attendingDatabase = FirebaseDatabase.getInstance();
        attendingRef = attendingDatabase.getReference("Games/" + g.key);
        AlertDialog.Builder builder = new AlertDialog.Builder(TeamGamesActivity.this);
        for (int i = 1 ; i<g.whoIsComming.size();i++)
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
            AlertDialog dialogAlert=builder.create();
            dialogAlert.show();
        }

    }

    @Override
    public void onButtonForces(int position, Game value) {
        retriveDataPlayers();

         Toast.makeText(TeamGamesActivity.this, "this fiture come tomorow",Toast.LENGTH_SHORT).show();
    }


    public void retriveDataPlayers() {//for btn who is comming
        database2.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                users = new ArrayList<User>();
                i=1;
                for (DataSnapshot data : snapshot.getChildren()) {
                    keyUser = (String) snapshot.child(String.valueOf(i)).getValue();//put in array of users at the teams key and not mail
                    //   Toast.makeText(context, String.valueOf(i),Toast.LENGTH_SHORT).show();
                    if (keyUser!=null &&keyUser!="-1")
                    {
                        teamDatabase = FirebaseDatabase.getInstance().getReference("Users/" + keyUser);
                        ValueEventListener valueEventListener = teamDatabase.addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {
                                User u = snapshot.getValue(User.class);
                                users.add(u);
                                snapshot.toString();
                                allPlayersAdapter.notifyDataSetChanged();
                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                        allPlayersAdapter = new AllUsersAdapter(TeamGamesActivity.this, 0, 0, users);
                        lv.setAdapter(allPlayersAdapter);
                    }
                    i++;
                }
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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



