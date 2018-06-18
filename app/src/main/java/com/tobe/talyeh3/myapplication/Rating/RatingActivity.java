package com.tobe.talyeh3.myapplication.Rating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tobe.talyeh3.myapplication.CreateGame.TeamGamesActivity;
import com.tobe.talyeh3.myapplication.Posts.Post;
import com.tobe.talyeh3.myapplication.R;

import com.tobe.talyeh3.myapplication.Team.OpenTeamDetails;
import com.tobe.talyeh3.myapplication.Team.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tobe.talyeh3.myapplication.ToBe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;

public class RatingActivity extends AppCompatActivity {
    CountDownLatch done = new CountDownLatch(1);
    ListView lv;
    int i = 0;
    String keyRating="";
    ArrayList<Rating> ratings;
    ArrayList<String> myRatings;
    RatingAdapter ratingAdapter;
    DatabaseReference ratingRef;
    private DatabaseReference database,ratingsDatabase;
    String keyTeam="";
    ProgressDialog progressDialog;
    String myUserId;
    TextView tvMyRate;
    FirebaseDatabase datab;
    Rating r;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_rating );
        getSupportActionBar().hide();
        tvMyRate=(TextView)findViewById(R.id.tvMyRate);
        Intent intent = getIntent();
        keyTeam = intent.getExtras().getString("teamKey");
        datab = FirebaseDatabase.getInstance();
        ratingRef = datab.getReference("Rating/" + keyTeam);
        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Teams/"+keyTeam+"/rating");
        lv = (ListView) findViewById( R.id.lv);
        if(database!=null)
        {
            this.retriveData();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rating r = ratings.get(position);
                Intent intent = new Intent(RatingActivity.this, EditRating.class);
                intent.putExtra("keyRating", r.key );
                startActivity(intent);
            }





        });

        retrieveData();


    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void retrieveData()
    {
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                r = new Rating(  );
                r = dataSnapshot.getValue( Rating.class );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }









    public void retriveData() {
        progressDialog.setMessage("load Please Wait...");
        progressDialog.show();

        database.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {
                myRatings = new ArrayList<String>();
                ratings = new ArrayList<Rating>();
                for (DataSnapshot data : snapshot.getChildren()) {



                    Log.d("onDataChange", data.getValue().toString());
                    keyRating = (String) snapshot.child(String.valueOf(i)).getValue();


                    if (keyRating == null)
                    {
                        return;
                    }

                   // Toast.makeText(RatingActivity.this, i+keyRating, Toast.LENGTH_LONG).show();
                    ratingsDatabase = FirebaseDatabase.getInstance().getReference("Rating/" + keyRating);
                    ValueEventListener valueEventListener = ratingsDatabase.addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot snapshot) {
                            Rating r = snapshot.getValue( Rating.class );

                            if (r==null)
                                return;

                            for (int j =0; j<ratings.size();j++)//for not duplicate on the screen
                            {
                                if(!r.equals(null)&&!ratings.get( j ).key.equals( null ) && ! r.key.equals(null)&&!r.equals(null))
                                {
                                        if(ratings.get( j ).key.equals( r.key ) &&!ratings.get( j ).equals( null ) )
                                            {
                                                  ratings.remove( j );
                                            }
                                }
                            }

                            String keyUser= myUserId.concat( keyTeam );//for the user cnnot rate himself

                            if (!r.key.equals(keyUser))
                                ratings.add( r );

                            else
                                tvMyRate.setText( "My average Rate is " + String.valueOf( r.avgRating ) );

                            Collections.sort(ratings, new Comparator<Rating>(){
                                public int compare(Rating obj1, Rating obj2)
                                {
                                    // TODO Auto-generated method stub
                                    return (obj1.avgRating > obj2.avgRating) ? -1: (obj1.avgRating > obj2.avgRating) ? 1:0 ;
                                }
                            });

                            ratingAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }


                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                    i++;
                }

                ratingAdapter = new RatingAdapter(RatingActivity.this, 0, 0, ratings,myUserId);
                lv.setAdapter(ratingAdapter);
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }





}
