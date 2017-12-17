package com.example.talyeh3.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ToBeTest extends AppCompatActivity implements View.OnClickListener
     {
    FirebaseAuth firebaseAuth;
    TextView btnAllUsers;
    TextView btnOpenTeam;
    TextView btnMyTeams;


    Dialog d;
    TextView logOut;
    Button btnMenu;
    int mode=0; // o means register 1 means login
    ProgressDialog progressDialog;
    TextView btnAllPost;
    TextView btnWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_to_be_test );



        firebaseAuth = FirebaseAuth.getInstance();
        btnAllPost = (TextView)findViewById(R.id.btnAllPost);
        btnWeather = (TextView)findViewById(R.id.btnWeather);

        btnMyTeams = (TextView)findViewById(R.id.btnMyTeams);
        progressDialog = new ProgressDialog(this);



        btnMenu = (Button)findViewById( R.id.btnMenu );
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            menu();

            }
        });

        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {

        }
        else//registeration page
        {
            Intent intent = new Intent(ToBeTest.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        btnOpenTeam = (TextView)findViewById(R.id.btnOpenTeam);
        btnOpenTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBeTest.this,OpenTeamDetails.class);
                startActivity(intent);

            }
        });

        btnAllPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBeTest.this,AllPostActivity.class);
                startActivity(intent);
            }
        });

        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBeTest.this,WeatherActivity.class);
                startActivity(intent);
            }
        });



        btnMyTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBeTest.this,MyTeams.class);
                startActivity(intent);
            }
        });



        btnAllUsers = (TextView)findViewById(R.id.btnAllUsers);
        btnAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBeTest.this,AllUsers.class);
                startActivity(intent);

            }
        });


    }



         public void menu()
         {
             d= new Dialog(this);
             d.setContentView(R.layout.menulayout);
             d.setCancelable(true);

             logOut = (TextView) d.findViewById(R.id.logOut);
             logOut.setOnClickListener(this);
             d.show();

         }


         @Override
         public void onClick(View view) {
             if (view==btnMenu)
             {
                 menu();
             }
             if(view==logOut)
             {
                 firebaseAuth.signOut();
                 Intent intent = new Intent(ToBeTest.this,RegisterActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
             }
         }
     }
