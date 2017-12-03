package com.example.talyeh3.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {


    ListView lv;
    ArrayList<User> users;
    AllUsersAdapter allUsersAdapter;
    TextView tvUserName;
    private DatabaseReference database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myUserId = user.getUid();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Users");
        lv = (ListView) findViewById(R.id.lv);
        tvUserName= (TextView) findViewById(R.id.tvUserName);
        this.retriveData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = users.get(position);
                Intent getIntent = getIntent();
                Intent intent = new Intent(AllUsers.this, ProfilePage.class);
                intent.putExtra("key", u.uid );
                startActivity(intent);
            }
        });

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
                allUsersAdapter = new AllUsersAdapter(AllUsers.this, 0, 0, users);
                lv.setAdapter(allUsersAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


