package com.example.talyeh3.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ToBe extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    Button btnAddPost,btnAllUsers;
    Button btnOpenTeam;
    Button btnLogOut,btnMyTeams;

    int mode=0; // o means register 1 means login
    ProgressDialog progressDialog;
    Button btnAllPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be);
        firebaseAuth = FirebaseAuth.getInstance();
        btnAllPost = (Button)findViewById(R.id.btnAllPost);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        btnMyTeams = (Button)findViewById(R.id.btnMyTeams);
        progressDialog = new ProgressDialog(this);
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {

        }
        else//registeration page
        {
           Intent intent = new Intent(ToBe.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        btnAddPost = (Button)findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,AddPostActivity.class);
                startActivity(intent);
            }
        });

        btnOpenTeam = (Button)findViewById(R.id.btnOpenTeam);
        btnOpenTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,OpenTeamDetails.class);
                startActivity(intent);

            }
        });

        btnAllPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,AllPostActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(this);


        btnMyTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,MyTeams.class);
                startActivity(intent);
            }
        });



        btnAllUsers = (Button)findViewById(R.id.btnAllUsers);
        btnAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,AllUsers.class);
                startActivity(intent);

            }
        });

    }
    public void onClick(View v) {

        if (v == btnLogOut) {
            firebaseAuth.signOut();
            Intent intent = new Intent(ToBe.this,RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
              }
    }

}





