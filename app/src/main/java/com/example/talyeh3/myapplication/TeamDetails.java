package com.example.talyeh3.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.Chat.ChatActivity;
import com.example.talyeh3.myapplication.CreateGame.CreateGame;
import com.example.talyeh3.myapplication.CreateGame.TeamGamesActivity;
import com.example.talyeh3.myapplication.Gallery.GalleryActivity;
import com.example.talyeh3.myapplication.Statistics.StatisticsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeamDetails extends AppCompatActivity implements View.OnClickListener{
    TextView tvName,btnAddPlayer,btnCreateGame,btnGames,btnStatistics,btnChat,btnGallery;
    ImageView btnTeamPlayers;
    FirebaseDatabase database;
    DatabaseReference teamRef;
    ImageView user_profile_photo;
    Team t;
    String key;
    String photo="";
    String teamName;
    private DatabaseReference databaseUser;

    Dialog d;
    int firstPress=0,addPlayer=0;
    ListView lv;
    int i = 0;
    private DatabaseReference database2,teamDatabase;
    ArrayList<User> users;
    String keyUser="";
    AllUsersAdapter allPlayersAdapter;
    FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
    String myUserId = us.getUid();



    User user,user2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_team_details);//try commit
        getSupportActionBar().hide();

        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        this.retriveData();
        Toast.makeText(TeamDetails.this, "sd  "+firstPress,Toast.LENGTH_SHORT).show();
        user_profile_photo=(ImageView)findViewById( R.id.user_profile_photo);
        database = FirebaseDatabase.getInstance();
        tvName = (TextView) findViewById( R.id.tvName);
        btnAddPlayer=(TextView)findViewById( R.id.btnAddPlayer );
        btnCreateGame=(TextView)findViewById( R.id.btnCreateGame );
        btnTeamPlayers=(ImageView) findViewById( R.id.btnTeamPlayers );
        btnStatistics=(TextView)findViewById( R.id.btnStatistics );
        btnChat=(TextView)findViewById( R.id.btnChat );
        btnGallery=(TextView)findViewById( R.id.btnGallery );
        btnGames=(TextView) findViewById( R.id.btnGames );
        btnAddPlayer.setOnClickListener( this );
        btnChat.setOnClickListener( this );
        btnGallery.setOnClickListener( this );
        btnStatistics.setOnClickListener( this );
        btnCreateGame.setOnClickListener( this );
        btnTeamPlayers.setOnClickListener( this );
        btnGames.setOnClickListener( this );
        Intent intent = getIntent();
        key = intent.getExtras().getString("keyteam");
        teamRef = database.getReference("Teams/" + key);
        this.retrieveData();
        Toast.makeText(TeamDetails.this, "hghg         "+key, Toast.LENGTH_LONG).show();
        //for all players team
        database2 = FirebaseDatabase.getInstance().getReference("Teams/"+key+"/users");


    }



    public void teamPlayers()
    {

        if(firstPress==0)
        {
            d= new Dialog(this);
            d.setContentView( R.layout.activity_all_users);
            d.setCancelable(true);
            lv = (ListView) d.findViewById( R.id.lv);
            this.retriveDataPlayers();
            d.show();
            Toast.makeText(TeamDetails.this, "a"+firstPress,Toast.LENGTH_SHORT).show();
        }

        else
            {
            d.show();
                Toast.makeText(TeamDetails.this, "b",Toast.LENGTH_SHORT).show();
        }
        d.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    d.cancel();
                    d.dismiss();
                    firstPress=1;
                }
                return true;
            }
        });
    }


    public void retrieveData()
    {
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                t = dataSnapshot.getValue(Team.class);
                teamName=t.name;
                tvName.setText("team name: " + t.name);
                photo= t.imgUrl;
                        Picasso
                        .with( TeamDetails.this )
                        .load( photo)
                        .fit() // will explain later
                        .into(user_profile_photo );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void retriveDataPlayers() {
        database2.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                users = new ArrayList<User>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    keyUser = (String) snapshot.child(String.valueOf(i)).getValue();//put in array of users at the teams key and not mail
                    //Toast.makeText(TeamDetails.this, "sd  "+firstPress,Toast.LENGTH_SHORT).show();
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

                    allPlayersAdapter = new AllUsersAdapter(TeamDetails.this, 0, 0, users);
                    lv.setAdapter(allPlayersAdapter);

                    i++;

                }

            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void onClick(View v) {
        if (v==btnAddPlayer)
        {
            startActivity(getIntent());
            addPlayer=1;
            Intent intent = new Intent( TeamDetails.this, OpenTeam.class );
            intent.putExtra( "teamKey", t.key );
            finish();
            startActivity( intent );
        }
        if(v==btnTeamPlayers)
        {
            teamPlayers();
        }
        if (v==btnCreateGame)
        {
            Intent intent = new Intent( TeamDetails.this, CreateGame.class );
            intent.putExtra( "teamKey", key );
            startActivity( intent );
        }
        if (v==btnStatistics)
        {
            Intent intent = new Intent( TeamDetails.this, StatisticsActivity.class );
            intent.putExtra( "teamKey", key );
            startActivity( intent );
        }
        if (v==btnGames)
        {
            Intent intent = new Intent( TeamDetails.this, TeamGamesActivity.class );
            intent.putExtra( "teamKey", key );
            startActivity( intent );
        }
        if (v==btnChat)
        {
            Intent intent = new Intent( TeamDetails.this, ChatActivity.class );
            intent.putExtra( "teamKey", key );
            intent.putExtra( "teamName", teamName );
            intent.putExtra( "profilePic", user2.imgUrl );
            intent.putExtra( "userName", user2.userName );
            startActivity( intent );
        }
        if (v==btnGallery)
        {
            Intent intent = new Intent( TeamDetails.this, GalleryActivity.class );
            intent.putExtra( "teamKey", key );
            intent.putExtra( "teamName", teamName );
            intent.putExtra( "profilePic", user2.imgUrl );
            intent.putExtra( "userName", user2.userName );
            startActivity( intent );
        }

    }


    public void retriveData() {
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                        user = data.getValue(User.class);
                    if (user.uid.equals( myUserId ))
                    {
                        user2 = user;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }





}
