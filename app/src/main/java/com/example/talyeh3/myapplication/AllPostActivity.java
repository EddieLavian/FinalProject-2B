package com.example.talyeh3.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.Posts.ThisPostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllPostActivity extends AppCompatActivity {

    ListView lv;
    String maneger="";
    ArrayList<Post> posts;
    AllPostAdapter allPostAdapter;
    Button btnAddPost;
    String myUserId="";
    private DatabaseReference database,databaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post);
        getSupportActionBar().hide();
        btnAddPost = (Button)findViewById(R.id.btnAddPost);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            myUserId =FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseUser = FirebaseDatabase.getInstance().getReference("Users/"+myUserId+"/managerSite");
            Toast.makeText(AllPostActivity.this, "jjj"+myUserId, Toast.LENGTH_LONG).show();
            databaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {//only managers add post
                    maneger = dataSnapshot.getValue(String.class);
                    if (maneger.equals( "yes" ))
                        btnAddPost.setVisibility(View.VISIBLE);
                    else
                        btnAddPost.setVisibility(View.INVISIBLE);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        database = FirebaseDatabase.getInstance().getReference("Posts");



        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllPostActivity.this,AddPostActivity.class);
                startActivity(intent);
            }
        });



        lv = (ListView) findViewById(R.id.lv);
        this.retriveData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Post p = posts.get(position);
                Intent intent = new Intent(AllPostActivity.this, EditPostActivity.class);
                intent.putExtra("key", p.key);
                startActivity(intent);
*/
                Post p = posts.get(position);
                Intent intent = new Intent(AllPostActivity.this,    ThisPostActivity.class);
                intent.putExtra("key", p.key );
                intent.putExtra("photo", p.imgUrl );
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Post p = posts.get(position);
                DatabaseReference current = FirebaseDatabase.getInstance().getReference("Posts/" + p.key);
                current.removeValue();
                return true;


            }
        });
    }


    public void retriveData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                posts = new ArrayList<Post>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post p = data.getValue(Post.class);
                    posts.add(p);

                }
                allPostAdapter = new AllPostAdapter(AllPostActivity.this, 0, 0, posts);
                lv.setAdapter(allPostAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

/*
    public void retrieveDataManeger()
    {
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                maneger = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    */
}
