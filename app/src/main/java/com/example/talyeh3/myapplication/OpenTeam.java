package com.example.talyeh3.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class OpenTeam extends AppCompatActivity {

    String teamKey;
    ListView lv;
    ArrayList<User> users;
    OpenTeamAdapter allUsersAdapter;
    TextView tvAddPlayer;
    private DatabaseReference database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myUserId = user.getUid();

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_team);
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Users");
        lv = (ListView) findViewById(R.id.lv);
        tvAddPlayer= (TextView) findViewById(R.id.tvAddPlayer);
        this.retriveData();
        AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = users.get(position);
                Intent getIntent = getIntent();
                teamKey = getIntent.getExtras().getString("teamKey");
               Intent intent = new Intent(OpenTeam.this, ProfileActivity.class);
               intent.putExtra("key", u.uid );
               intent.putExtra("team",teamKey);
               startActivity(intent);


            }





        });







        setupAutoComplete(tv,users);
    }

    public void retriveData() {
        progressDialog.setMessage("load Please Wait...");
        progressDialog.show();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users = new ArrayList<User>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //if (data.getValue(User.class).uid!=myUserId)
                    {
                        User u = data.getValue(User.class);
                        users.add(u);
                    }


                }
                progressDialog.dismiss();
                allUsersAdapter = new OpenTeamAdapter(OpenTeam.this, 0, 0, users);
                lv.setAdapter(allUsersAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void setupAutoComplete(AutoCompleteTextView view, List<User> objects) {
        List<String> names = new AbstractList<String>() {
            public int size() { return users.size(); }
            public String get(int location) {

                return users.get(location).userName;
            }
        };
        view.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
    }


}


