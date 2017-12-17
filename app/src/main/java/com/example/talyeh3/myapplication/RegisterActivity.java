package com.example.talyeh3.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST =234;
    private StorageReference mStorageRef;
    private Uri filePath;
    FirebaseAuth firebaseAuth;
    Button btnMainRegister,btnMainLogin,btnChoose;
    ImageView imgProfile;
    EditText etEmail,etPass,etUserName,etAge, etRePass;
    Dialog d;
    CardView cvLogIn;
    Button btnReg,btnLogin;
    ProgressDialog progressDialog;
    Boolean b=true;//if the user uploded photo
    String generatedFilePath="https://firebasestorage.googleapis.com/v0/b/tobe-722db.appspot.com/o/images%2Fprofile.jpg?alt=media&token=da8a59c5-92b4-41d8-8453-bc64b9a3d1b8";
    TextView tvRegister;
    //for user datails will save on data base
    FirebaseDatabase database;
    Spinner spin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        cvLogIn = (CardView)findViewById(R.id.cvLogIn);
        //btnMainLogin.setOnClickListener(this);
        tvRegister = (TextView)findViewById(R.id.tvRegister);
       tvRegister.setOnClickListener(this);

        etEmail=(EditText)findViewById(R.id.etEmail);
        etPass=(EditText)findViewById(R.id.etPass);
        cvLogIn = (CardView)findViewById(R.id.cvLogIn);
        cvLogIn.setOnClickListener(this);
    }






    public void createRegisterDialog()
    {
        d= new Dialog(this);
        d.setContentView(R.layout.registerlayout);
        d.setTitle("Register");
        d.setCancelable(true);
        etEmail=(EditText)d.findViewById(R.id.etEmail);
        etPass=(EditText)d.findViewById(R.id.etPass);
        etAge=(EditText)d.findViewById(R.id.etAge);
        etRePass=(EditText)d.findViewById(R.id.etRePass);
        etUserName=(EditText)d.findViewById(R.id.etUserName);
        btnReg=(Button)d.findViewById(R.id.btnRegister);
        imgProfile = (ImageView) d.findViewById(R.id.imgProfile);
        btnChoose=(Button)d.findViewById(R.id.btnChoose);
        spin = (Spinner) d.findViewById(R.id.spinner);
        btnReg.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
        d.show();

    }

    private void uploadFile(final String uid) {

        if (filePath != null) {
            b=false;
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images/"+uid+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"File uploadedgg",Toast.LENGTH_LONG).show();


                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            generatedFilePath = downloadUri.toString();
                            List<String> myTeams;
                            myTeams = new ArrayList<String>();
                            myTeams.add("-1");
                            User u = new User(uid,etUserName.getText().toString(),etEmail.getText().toString(),Integer.valueOf(etAge.getText().toString()),"",myTeams,generatedFilePath, spin.getSelectedItem().toString(),0);
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Users").child(uid).setValue(u);

                            Intent intent = new Intent(RegisterActivity.this, ToBeTest.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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



    public void register()
    {

        if(etUserName.getText().length() < 1 || etEmail.getText().length() < 1 || etPass.getText().length() < 1 || etRePass.getText().length() < 1 || etAge.getText().length()< 1)
        {
            Toast.makeText(RegisterActivity.this, "Some Fields Are Empty", Toast.LENGTH_LONG).show();
            return;
        }
        else if(etPass.getText().toString().length() < 6)
        {
            Toast.makeText(RegisterActivity.this, "Password must be six Charcters or more.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!etPass.getText().toString().equals(etRePass.getText().toString()))
        {
            Toast.makeText(RegisterActivity.this, "Not same passwords. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!etEmail.getText().toString().matches(emailPattern))
        {
            Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();

                    //for user datails will save on data base
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Toast.makeText(RegisterActivity.this, uid,Toast.LENGTH_SHORT).show();
                    List<String> myTeams;
                    myTeams = new ArrayList<String>();
                    myTeams.add("-1");



                    uploadFile(uid);


                    if(b==true)
                    {
                        User u = new User(uid,etUserName.getText().toString(),etEmail.getText().toString(),Integer.valueOf(etAge.getText().toString()),"",myTeams,generatedFilePath, spin.getSelectedItem().toString(),0);
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("Users").child(uid).setValue(u);

                        Intent intent = new Intent(RegisterActivity.this, ToBeTest.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                }

                d.dismiss();
                progressDialog.dismiss();


            }
        });


    }

    public void login()
    {
        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "auth_success!!!!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, ToBeTest.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "auth_failed",Toast.LENGTH_SHORT).show();

                        }
           progressDialog.dismiss();

                    }
                });

    }


    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);


    }

    @Override

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

    public void onClick(View v) {

        if(v==btnMainLogin)
        {

            if(btnMainLogin.getText().toString().equals("Login"))
            {
                //createLoginDialog();
            }
            else if(btnMainLogin.getText().toString().equals("Logout"))
            {
                firebaseAuth.signOut();
                btnMainLogin.setText("Login");
            }

        }
        else if(v==tvRegister)
        {
            createRegisterDialog();
        }
        else if (btnReg==v)
        {
            register();
        }
        else if(v==cvLogIn)
        {
            login();
        }
        else if(v==btnChoose)
        {
            showFileChooser();
        }

    }

}

