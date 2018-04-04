package com.example.talyeh3.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.Chat.ChatActivity;
import com.example.talyeh3.myapplication.Posts.AllPostActivity;
import com.example.talyeh3.myapplication.Team.MyTeams;
import com.example.talyeh3.myapplication.Team.OpenTeamDetails;
import com.example.talyeh3.myapplication.Weather.WeatherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.Manifest.permission.SEND_SMS;

public class ToBeTest extends AppCompatActivity implements View.OnClickListener
     {
    FirebaseAuth firebaseAuth;
    TextView btnAllUsers;
    TextView btnOpenTeam;
    TextView btnMyTeams;
    TextView btnChat;
    TextView btnAddFriends;

    Dialog d;
    TextView logOut,MyProfile;
    Button btnMenu;
    int mode=0; // o means register 1 means login
    ProgressDialog progressDialog;
    TextView btnAllPost;
    TextView btnWeather;
    String name="";
    FirebaseUser us;
    String myUserId;
    private DatabaseReference databaseUser;
    User user,user2;
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_SMS = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_to_be_test );

       // FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        //FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            us= FirebaseAuth.getInstance().getCurrentUser();
            myUserId = us.getUid();
        }
        else//registeration page
        {
            Intent intent = new Intent(ToBeTest.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        databaseUser = FirebaseDatabase.getInstance().getReference("Users");

        // get permissions to user location
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        //ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_SMS);

        btnAllPost = (TextView)findViewById(R.id.btnAllPost);
        btnWeather = (TextView)findViewById(R.id.btnWeather);
         retriveData();
        btnMyTeams = (TextView)findViewById(R.id.btnMyTeams);
        progressDialog = new ProgressDialog(this);



        btnMenu = (Button)findViewById( R.id.btnMenu );
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            menu();

            }
        });


        btnOpenTeam = (TextView)findViewById(R.id.btnOpenTeam);
        btnOpenTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBeTest.this,OpenTeamDetails.class);
                startActivity(intent);

            }
        });

        btnChat = (TextView)findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user2== null)
                {
                    Toast.makeText(ToBeTest.this, "The data will be updated now, please click again ", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(ToBeTest.this,ChatActivity.class);
                intent.putExtra( "profilePic", user2.imgUrl );
                intent.putExtra( "userName", user2.userName );
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

        btnAddFriends = (TextView)findViewById(R.id.btnAddFriends);
        btnAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
        // Ask you only once how you want to share //
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "בוא להיות חלק מהמשחק. הורד את ToBe עכשיו מהחנות");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
*/

        //Ask you how you want to share again and again //
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "בוא להיות חלק מהמשחק. הורד את האפליקציה ToBe עוד היום ותתחבר לשחקנים האמיתיים");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.project_id)));


 /* // Intent to new wendw with sms sender
                Intent intent = new Intent(AllUsers.this,AddFriendsActivity.class);
                startActivity(intent);
 */

 /* intent to waze
 try
{
  // Launch Waze to look for Hawaii:
  String url = "https://waze.com/ul?q=Hawaii";
  Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
  startActivity( intent );
}
catch ( ActivityNotFoundException ex  )
{
  // If Waze is not installed, open it in Google Play:
  Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
  startActivity(intent);
}
  */

            }
        });


    }



         public void menu()
         {
             d= new Dialog(this);
             d.setContentView(R.layout.menulayout);
             d.setCancelable(true);

             logOut = (TextView) d.findViewById(R.id.logOut);
             MyProfile = (TextView) d.findViewById(R.id.MyProfile);
             logOut.setOnClickListener(this);
             MyProfile.setOnClickListener(this);
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
             if(view==MyProfile)
             {
                 Intent intent = new Intent(ToBeTest.this, ProfilePage.class);
                 intent.putExtra("key", myUserId);
                 d.dismiss();
                 startActivity(intent);
             }
         }



         public void retriveData() {
             databaseUser.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     // myId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
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
