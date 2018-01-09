package com.example.talyeh3.myapplication.Posts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.talyeh3.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPostActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etTitle,etBody,etSubTitle;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference postRef;
    String key;
    Post p;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_edit_post);

        database = FirebaseDatabase.getInstance();
        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);
        etSubTitle = (EditText) findViewById(R.id.etSubtitle);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();

        key = intent.getExtras().getString("key");
        postRef = database.getReference("Posts/" + key);
        Toast.makeText(EditPostActivity.this, "user: " + postRef, Toast.LENGTH_LONG).show();
        this.retrieveData();
    }


    public void retrieveData()
    {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                p = dataSnapshot.getValue(Post.class);
                etBody.setText(p.body);
                etTitle.setText(p.title);
                etSubTitle.setText(p.subtitle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onClick(View v) {
        postRef = database.getReference("Posts/" + p.key);

        p.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        p.title = etTitle.getText().toString();
        p.body = etBody.getText().toString();
        p.subtitle=etSubTitle.getText().toString();
        p.likes = 0;
        postRef.setValue(p);

        finish();



    }
}
