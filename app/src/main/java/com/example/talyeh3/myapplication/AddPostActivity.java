package com.example.talyeh3.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etTitle,etBody;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference postRef;

    String key;
    Post p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        database = FirebaseDatabase.getInstance();
        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().toString();
        Post p = new Post(uid,etTitle.getText().toString(),etBody.getText().toString(),0,"");
        postRef = database.getReference("Posts").push();
        p.key = postRef.getKey();
        postRef.setValue(p);

        finish();

    }
}
