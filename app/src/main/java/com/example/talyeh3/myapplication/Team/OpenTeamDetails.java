package com.example.talyeh3.myapplication.Team;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Statistics.Statistics;
import com.example.talyeh3.myapplication.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;
import java.util.List;

public class OpenTeamDetails extends AppCompatActivity implements View.OnClickListener {

    EditText etTeamName;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference teamRef, userRef, userRef2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String myUserId = user.getUid();
    ArrayList<String> myTeams;
    User u;
    Spinner spin;
    ImageView imgProfile;
    String generatedFilePath = "https://firebasestorage.googleapis.com/v0/b/tobe-722db.appspot.com/o/images%2Fteam.png?alt=media&token=032ec4ea-80a1-476b-befc-c8caeda0c3a2";
    Button btnChoose;
    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference mStorageRef;
    private Uri filePath;
    Boolean b = true;//if the user uploded photo
    ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_open_team_details );
        database = FirebaseDatabase.getInstance();
        etTeamName = (EditText) findViewById( R.id.etTeamName );
        btnSave = (Button) findViewById( R.id.btnSave );
        btnSave.setOnClickListener( this );
        userRef = database.getReference( "Users/" + myUserId );
        getSupportActionBar().hide();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog( this );
        imgProfile = (ImageView) findViewById( R.id.imgProfile );
        btnChoose = (Button) findViewById( R.id.btnChoose );
        btnChoose.setOnClickListener(this);
        spin = (Spinner) findViewById( R.id.spinner);
        this.retriveData();
    }

    private void retriveData() {
        userRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue( User.class );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }


    @Override
    public void onClick(View view) {
        if (view == btnSave)
        {
            if(etTeamName.getText().length()<=0)
            {
                Toast.makeText(OpenTeamDetails.this, "Team Name Is Empty", Toast.LENGTH_LONG).show();
                return;
            }
        String uid = FirebaseAuth.getInstance().getCurrentUser().toString();
        List<String> players;
        players = new ArrayList<String>();
        players.add( FirebaseAuth.getInstance().getCurrentUser().getUid());
        List<String> games;
        games= new ArrayList<String>();
        games.add("-1");
        String myUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String myUserMail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        List<String> statistics;
        statistics= new ArrayList<String>();
        statistics.add( "-1" );
        Team t = new Team( uid, etTeamName.getText().toString(), players,games, "", generatedFilePath ,spin.getSelectedItem().toString(),statistics);
        teamRef = database.getReference( "Teams" ).push();
            userRef = database.getReference( "Users/" + myUserId );
        t.key = teamRef.getKey();
        statistics.add(myUserKey+t.key);
            statistics.remove( "-1" );
        t.statistics=statistics;
        Statistics s=new Statistics( myUserKey+t.key,t.key,u.userName,0,0,0);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Statistics").child(myUserKey+t.key).setValue(s);

        myTeams = new ArrayList<String>();
        userRef2 = database.getReference( "Users/" + myUserId + "/teams/0" );

        // Toast.makeText(OpenTeamDetails.this,  userRef2.toString(), Toast.LENGTH_LONG).show();
        u.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();//נהות
        u.teams.add( t.key );
        userRef.setValue( u );
        if (userRef2 != null) {
            userRef2.removeValue();
        }

        uploadFile( t);
        teamRef.setValue( t );
        if (b == true) {
            t.imgUrl=generatedFilePath;
            teamRef.setValue( t );
            Intent intent = new Intent( OpenTeamDetails.this, OpenTeam.class );
            intent.putExtra( "teamKey", t.key );
            startActivity( intent );
            finish();
        }



    }
        else if(view==btnChoose)
        {
            showFileChooser();
        }

}



    private void uploadFile(final Team t) {

        if (filePath != null) {
            b=false;
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images/"+t.key+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();


                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            t.imgUrl= downloadUri.toString();
                            teamRef.setValue( t );
                            Intent intent = new Intent( OpenTeamDetails.this, OpenTeam.class );
                            intent.putExtra( "teamKey", t.key );
                            startActivity( intent );
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
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }










}
