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
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.AllUsersAdapter;
import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.Posts.Post;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Rating.ForcesAdapter;
import com.example.talyeh3.myapplication.Rating.Rating;
import com.example.talyeh3.myapplication.Rating.RatingAdapter;
import com.example.talyeh3.myapplication.Team.Team;
import com.example.talyeh3.myapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TeamGamesActivity extends AppCompatActivity implements com.example.talyeh3.myapplication.CreateGame.TeamGamesAdapter.customButtonListener {
    ListView lv,lv1,lv2;
    int i = 1,j=0;
    ArrayList<Game> games;
    ArrayList<String> myGames;
    TeamGamesAdapter TeamGamesAdapter;
    private DatabaseReference database,gameDatabase,database2,teamDatabase,teamDatabase2;
    ProgressDialog progressDialog;
    String keyTeam,keyGame;
    DatabaseReference attendingRef;
    FirebaseDatabase attendingDatabase;
    Game g;
    Boolean ifAttending=true,checkIfAttending=false,first=true;
    String myUserId;
    Dialog dialog;

    int count = 0;
    AllUsersAdapter allPlayersAdapter;
    ForcesAdapter allRatingAdapter;
    ArrayList<User> users;
    ArrayList<Rating> ratings1,ratings2;
    String keyUser,keyUserRating;
    String howMouchPlayers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_team_games );
        Intent intent = getIntent();
        howMouchPlayers = intent.getExtras().getString("k");
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


                                Toast.makeText(TeamGamesActivity.this, "hi", Toast.LENGTH_LONG).show();
                                for (int j = 0; j<games.size();j++)
                                {
                                    if (g.key.equals(games.get(j).key))
                                        games.remove(j);
                                }
                                games.add( g );
                                Log.d( "onStart", snapshot.toString() );





                            }
                            if (TeamGamesAdapter!=null)
                            {
                                TeamGamesAdapter.notifyDataSetChanged();

                            }

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
        int howMouch =Integer.parseInt(howMouchPlayers);
        howMouch=howMouch*2;
        if (value.attending<howMouch)
       {
            Toast.makeText( TeamGamesActivity.this,"Not enough players confirmed arrival", Toast.LENGTH_SHORT ).show();
            return;
        }

        database2 = FirebaseDatabase.getInstance().getReference( "Games/" + value.key + "/whoIsComming" );
        // Toast.makeText(context, tvPlayers.getParent().toString(),Toast.LENGTH_SHORT).show();
        retriveDataForces(howMouch);
        dialog.setContentView( R.layout.forces );
        dialog.setCancelable( true );
        lv1 = (ListView) dialog.findViewById( R.id.lv1 );
        lv2 = (ListView) dialog.findViewById( R.id.lv2 );
        dialog.show();
    }

    public void retriveDataForces(final int howMouch) {
        database2.addValueEventListener( new ValueEventListener() {
            public ViewGroup getListView() {
                return lv1;
            }
            public ViewGroup getListView2() {
                return lv2;
            }

            public void onDataChange(DataSnapshot snapshot) {
                ratings1 = new ArrayList<Rating>();
                ratings2 = new ArrayList<Rating>();
                i = 1;
                for (DataSnapshot data : snapshot.getChildren()) {
                    keyUserRating = (String) snapshot.child( String.valueOf( i ) ).getValue();//put in array of users at the teams key and not mail
                    //   Toast.makeText(context, String.valueOf(i),Toast.LENGTH_SHORT).show();
                    if (keyUserRating != null && keyUserRating != "-1") {
                        String key = keyUserRating + keyTeam;

                        teamDatabase2 = FirebaseDatabase.getInstance().getReference( "Rating/" + key );
                        j=0;
                        ValueEventListener valueEventListener = teamDatabase2.addValueEventListener( new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {
                                Rating r = snapshot.getValue( Rating.class );


                                if (j%2==0 &&j<howMouch)
                                {

                                    ratings1.add( r );
                                    Collections.sort( ratings1, new Comparator<Rating>() {
                                        public int compare(Rating obj1, Rating obj2) {
                                            // TODO Auto-generated method stub
                                            return (obj1.avgRating > obj2.avgRating) ? -1 : (obj1.avgRating > obj2.avgRating) ? 1 : 0;
                                        }
                                    } );
                                }
                                else if(j%2!=0 &&j<howMouch)
                                {
                                    ratings2.add( r );
                                    Toast.makeText( TeamGamesActivity.this, String.valueOf( i ), Toast.LENGTH_SHORT ).show();
                                    Collections.sort( ratings2, new Comparator<Rating>() {
                                        public int compare(Rating obj1, Rating obj2) {
                                            // TODO Auto-generated method stub
                                            return (obj1.avgRating > obj2.avgRating) ? -1 : (obj1.avgRating > obj2.avgRating) ? 1 : 0;
                                        }
                                    } );
                                }





                                allRatingAdapter.notifyDataSetChanged();

                                j++;

                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }

                        } );

                    }
                    i++;
                }


                allRatingAdapter = new ForcesAdapter( TeamGamesActivity.this, 0, 0, ratings1 );
                lv1.setAdapter( allRatingAdapter );
                allRatingAdapter = new ForcesAdapter( TeamGamesActivity.this, 0, 0, ratings2 );
                lv2.setAdapter( allRatingAdapter );
                LayoutAnimationController controller
                        = AnimationUtils.loadLayoutAnimation(
                        TeamGamesActivity.this, R.anim.list_layout_controller);
                getListView().setLayoutAnimation(controller);
                LayoutAnimationController controller2
                        = AnimationUtils.loadLayoutAnimation(
                        TeamGamesActivity.this, R.anim.list_layout_controller);
                getListView2().setLayoutAnimation(controller2);
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        } );

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



