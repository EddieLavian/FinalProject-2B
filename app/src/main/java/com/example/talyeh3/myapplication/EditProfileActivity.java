package com.example.talyeh3.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.talyeh3.myapplication.Posts.EditPostActivity;
import com.example.talyeh3.myapplication.Posts.Post;
import com.example.talyeh3.myapplication.Team.OpenTeam;
import com.example.talyeh3.myapplication.Team.OpenTeamDetails;
import com.example.talyeh3.myapplication.Team.Team;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etUserName,etAge;
    Spinner spin;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference userRef;
    String key;
    User u;
    Button btnChoose;


    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference mStorageRef;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_profile );
        getSupportActionBar().hide();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        etUserName = (EditText) findViewById(R.id.etUserName);
        etAge = (EditText) findViewById(R.id.etAge);
        spin = (Spinner) findViewById(R.id.spinner);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();

        key = intent.getExtras().getString("key");
        userRef = database.getReference("Users/" + key);
        //Toast.makeText(EditProfileActivity.this, key, Toast.LENGTH_LONG).show();
        this.retrieveData();
        btnChoose = (Button) findViewById( R.id.btnChoose );
        btnChoose.setOnClickListener(this);
    }

    public void retrieveData()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                u = dataSnapshot.getValue(User.class);
                etAge.setText(String.valueOf( u.age ));
                etUserName.setText(u.userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View v) {
        if(v==btnSave)
        {
            userRef = database.getReference("Users/" + key);
            u.age = Integer.valueOf(etAge.getText().toString())  ;
            u.city=spin.getSelectedItem().toString();
            u.userName=etUserName.getText().toString();
            uploadFile();
            userRef.setValue(u);
            if (filePath==null)
                finish();

        }
        else if(v==btnChoose)
        {
            showFileChooser();
        }

    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST &&resultCode==RESULT_OK&&data!=null && data.getData()!=null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images/"+key+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();


                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            u.imgUrl= downloadUri.toString();
                            userRef.setValue(u);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int)progress)+"% Uploaded...");
                        }
                    })
            ;

        }

    }

}
