package com.example.talyeh3.myapplication.Team;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.talyeh3.myapplication.AllUsersAdapter;
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

public class OpenTeam extends AppCompatActivity {

    String teamKey;
    ListView lv;
    ArrayList<User> users;
    AllUsersAdapter allUsersAdapter;
    TextView tvAddPlayer;
    ImageView image;
    private DatabaseReference database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myUserId = user.getUid();
    Button btn;
    String keyUser="";
    ProgressDialog progressDialog;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_open_team);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Users");
        lv = (ListView) findViewById( R.id.lv);
        //tvAddPlayer= (TextView) findViewById( R.id.tvAddPlayer);
        this.retriveData();


/*
        List<String> names = new AbstractList<String>() {
            public int size() { return users.size(); }
            public String get(int location) {
                return users.get(location).userName;
            }
        };
        final AutoCompleteTextView tv = (AutoCompleteTextView) findViewById( R.id.autoCompleteTextView);
        ArrayAdapter<String >adapter=new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, names );
        tv.setAdapter( adapter );
        image = (ImageView) findViewById( R.id.image);
        image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.showDropDown();
            }
        } );
        btn= (Button) findViewById( R.id.btn);
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = tv.getText().toString();
            }
        } );
*/

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = users.get(position);
                Intent getIntent = getIntent();
                teamKey = getIntent.getExtras().getString("teamKey");
                Intent intent = new Intent(OpenTeam.this, ProfileActivity.class);
                intent.putExtra("key", u.uid );
                intent.putExtra("team",teamKey);
                intent.putExtra("photo", u.imgUrl );
                startActivity(intent);

            }

        });
/*
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                User u =users.get(position);
                DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("Users/teams/" +teamKey);
                DatabaseReference currentTeam = FirebaseDatabase.getInstance().getReference("Teams/users/" + u.uid);
                currentUser.removeValue();
                currentTeam.removeValue();
                return true;
            }
        });
*/

        //   setupAutoComplete(tv,users);


    }

    public void retriveData() {
        progressDialog.setMessage("load Please Wait...");
        progressDialog.show();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<User>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User u = data.getValue(User.class);
                    int notDuplicateUser=0;
                        /*
                        for (int i = 0; i < users.size();i++)
                        {
                            if (u.uid.equals(users.get(i).uid))
                            {
                                users.remove( i );
                                notDuplicateUser=-1;
                            }
                        }
                        */

                    if (!u.uid.equals( myUserId )&& notDuplicateUser==0)
                        users.add(u);
                }
                progressDialog.dismiss();
                allUsersAdapter = new AllUsersAdapter(OpenTeam.this, 0, 0, users);
                lv.setAdapter(allUsersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    /*
    private void setupAutoComplete(AutoCompleteTextView view, List<User> objects) {
        List<String> names = new AbstractList<String>() {
            public int size() { return users.size(); }
            public String get(int location) {
                return users.get(location).userName;
            }
        };
        view.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
    }
    */

}