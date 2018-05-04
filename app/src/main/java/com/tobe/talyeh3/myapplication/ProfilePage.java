package com.tobe.talyeh3.myapplication;

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

import com.tobe.talyeh3.myapplication.Posts.EditPostActivity;
import com.tobe.talyeh3.myapplication.Posts.ThisPostActivity;
import com.tobe.talyeh3.myapplication.Team.MyTeamsAdapter;
import com.tobe.talyeh3.myapplication.Team.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity implements View.OnClickListener{
    TextView tvUserName,tvAge,tvCity,tvTeams, tvUpcomingGames, tvMyStatistics;
    FirebaseDatabase database;
    DatabaseReference userRef;
    ImageView editProfile;
    String key,photo;
    User u;
    ImageView imgProfile;
    String myUserId;

    ListView lv;
    int i = 1;
    String keyteam="";
    ArrayList<Team> teams;
    ArrayList<String> myTeams;
    MyTeamsAdapter allTeamsAdapter;
    private DatabaseReference database2,teamDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    Dialog d;
    int firstPress=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_profile_page);
        getSupportActionBar().hide();
        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imgProfile = (ImageView)findViewById( R.id.imgProfile);
        database = FirebaseDatabase.getInstance();
        tvUserName = (TextView) findViewById( R.id.tvUserName);
        editProfile = (ImageView) findViewById(R.id.editProfile);
       // tvTeam = (TextView) findViewById(R.id.tvTeam);
        tvTeams = (TextView) findViewById( R.id.tvTeams);
        tvUpcomingGames = (TextView) findViewById( R.id.tvUpcomingGames);
        tvMyStatistics = (TextView) findViewById( R.id.tvMyStatistics);
        tvCity = (TextView) findViewById( R.id.tvCity);
        tvAge = (TextView) findViewById( R.id.tvAge);
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");
        photo = intent.getExtras().getString("photo");

        if(myUserId.equals( key ))
        {
            editProfile.setVisibility( View.VISIBLE);
            editProfile.setVisibility( View.VISIBLE);
        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, EditProfileActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);

            }
        });
        userRef = database.getReference("Users/" + key);
        tvTeams.setOnClickListener(this);
        tvUpcomingGames.setOnClickListener(this);
        tvMyStatistics.setOnClickListener(this);

        Picasso
                .with( ProfilePage.this )
                .load( photo)
                .fit() // will explain later
                .into( imgProfile );

        this.retrieveData();

        database2 = FirebaseDatabase.getInstance().getReference("Users/"+key+"/teams");

        //teamDatabase = FirebaseDatabase.getInstance().getReference("Teams").push();
    }




    public void myTeams()
    {
        if(firstPress==0)
        {
            d= new Dialog(this);
            d.setContentView( R.layout.activity_my_teams);
            d.setCancelable(true);
            lv = (ListView) d.findViewById( R.id.lv);
            this.retriveDataTeams();
            d.show();
        }
        else
            d.show();

        d.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    d.cancel();
                    firstPress=1;
                }
                return true;
            }
        });
    }

    public void retrieveData()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                photo=u.imgUrl;
                tvUserName.setText(u.userName + ", " + String.valueOf(u.age)); // userName, age
                //tvAge.setText(String.valueOf(u.age) + " years old");
                tvCity.setText("From  "+String.valueOf(u.city));
                Picasso
                        .with( ProfilePage.this )
                        .load( photo)
                        .fit() // will explain later
                        .into( imgProfile );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void retriveDataTeams() {
        database2.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                myTeams = new ArrayList<String>();
                teams = new ArrayList<Team>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    keyteam = (String) snapshot.child(String.valueOf(i)).getValue();
                    teamDatabase = FirebaseDatabase.getInstance().getReference("Teams/" + keyteam);


                    ValueEventListener valueEventListener = teamDatabase.addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot snapshot) {
                            Team t = snapshot.getValue(Team.class);
                            teams.add(t);
                            snapshot.toString();
                            allTeamsAdapter.notifyDataSetChanged();
                        }

                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                    allTeamsAdapter = new MyTeamsAdapter(ProfilePage.this, 0, 0, teams);
                    lv.setAdapter(allTeamsAdapter);

                    i++;

                }

            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void onClick(View v) {

        if(v==tvTeams)
        {
            myTeams();
        }
        if(v == tvUpcomingGames)
        {
            Toast.makeText(this, "This Feature Will Be Able Soon", Toast.LENGTH_LONG).show();
        }
        if (v== tvMyStatistics)
        {
            Toast.makeText(this, "This Feature Will Be Able Soon", Toast.LENGTH_LONG).show();
        }
    }

}