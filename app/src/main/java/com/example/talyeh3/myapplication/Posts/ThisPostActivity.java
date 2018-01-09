package com.example.talyeh3.myapplication.Posts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ThisPostActivity extends AppCompatActivity {
    TextView tvTitle,tvSubTitle,tvBody;
    FirebaseDatabase database;
    DatabaseReference postRef;
    String key,photo;
    Post p;
    ImageView imgProfile;

    TextView btnEditPost,btnDelitePost;
    String maneger="";
    String myUserId="";
    private DatabaseReference databaseUser;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_this_post );
        getSupportActionBar().hide();




        imgProfile = (ImageView)findViewById( R.id.imgProfile);
        database = FirebaseDatabase.getInstance();

        tvTitle = (TextView) findViewById( R.id.tvTitle);
        tvSubTitle = (TextView) findViewById( R.id.tvSubTitle);
        tvBody = (TextView) findViewById( R.id.tvBody);
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");
        photo = intent.getExtras().getString("photo");
        postRef = database.getReference("Posts/" + key);

        btnEditPost = (TextView)findViewById(R.id.btnEditPost);
        btnDelitePost = (TextView)findViewById(R.id.btnDelitePost);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseUser = FirebaseDatabase.getInstance().getReference("Users/"+myUserId+"/managerSite");
            Toast.makeText(ThisPostActivity.this, "jjj"+myUserId, Toast.LENGTH_LONG).show();
            databaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {//only managers add post
                    maneger = dataSnapshot.getValue(String.class);
                    if (maneger.equals( "yes" ))
                    {
                        btnDelitePost.setVisibility( View.VISIBLE);
                        btnEditPost.setVisibility( View.VISIBLE);
                    }

                    else
                    {
                        btnDelitePost.setVisibility(View.INVISIBLE);
                        btnEditPost.setVisibility( View.INVISIBLE);
                    }


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


        Toast.makeText(ThisPostActivity.this,key, Toast.LENGTH_LONG).show();

        Picasso
                .with( ThisPostActivity.this )
                .load( photo)
                .fit() // will explain later
                .into( imgProfile );

        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisPostActivity.this, EditPostActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });


        btnDelitePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThisPostActivity.this, AllPostActivity.class);
                startActivity(intent);
                DatabaseReference current = FirebaseDatabase.getInstance().getReference("Posts/" + key);
                current.removeValue();
                finish();
            }
        });

        this.retrieveData();

    }
    public void retrieveData()
    {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(Post.class);
                if (p!=null)
                {
                    tvTitle.setText(p.title);
                    tvSubTitle.setText(String.valueOf(p.subtitle));
                    tvBody.setText(String.valueOf(p.body));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
