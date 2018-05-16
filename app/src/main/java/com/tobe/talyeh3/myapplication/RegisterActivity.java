package com.tobe.talyeh3.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
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
    EditText etEmailLogin,etPassLogin,etUserName,etAge, etRePass, etEmailReg, etPassReg;
    Dialog d;
    CardView cvLogIn;
    ImageView btnfacebook;
    Button btnReg,btnLogin;
    ProgressDialog progressDialog;
    Boolean b=true;//if the user uploded photo
    String generatedFilePath="https://firebasestorage.googleapis.com/v0/b/tobe-722db.appspot.com/o/appImages%2Fprofile.jpg?alt=media&token=04178b40-4678-441b-adca-3d3d1dde15c9";
    TextView tvRegister;
    //for user datails will save on data base
    FirebaseDatabase database;
    Spinner spin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private DatabaseReference mUserDatabase;

    // All users
    /*
    ArrayList<String> users;
    private DatabaseReference databaseUsers;
    */

    public static String FACEBOOK_URL = "https://www.facebook.com/eddie.lavian.9";
    public static String FACEBOOK_PAGE_ID = "eddie.lavian.9";

    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

        // All users
        /*
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        users = new ArrayList<String>();
        */

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child( "Users" );
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        cvLogIn = (CardView)findViewById(R.id.cvLogIn);
        //btnMainLogin.setOnClickListener(this);
        tvRegister = (TextView)findViewById(R.id.tvRegister);
       tvRegister.setOnClickListener(this);
       btnfacebook = (ImageView)findViewById(R.id.btnfacebook);

        etEmailLogin=(EditText)findViewById(R.id.etEmailLogin);
        etPassLogin=(EditText)findViewById(R.id.etPassLogin);
        cvLogIn = (CardView)findViewById(R.id.cvLogIn);
        cvLogIn.setOnClickListener(this);
        btnfacebook.setOnClickListener(this);
    }

/*
    public void retriveData()
    {
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    {
                        User u = data.getValue(User.class);
                        users.add(u.userName);
                        Toast.makeText(RegisterActivity.this, u.userName, Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
*/


    public void createRegisterDialog()
    {
//        this.retriveData();
        d= new Dialog(this);
        d.setContentView(R.layout.registerlayout);
        d.setTitle("Register");
        d.setCancelable(true);
        etEmailReg=(EditText)d.findViewById(R.id.etEmailReg);
        etPassReg=(EditText)d.findViewById(R.id.etPassReg);
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
                            Toast.makeText(getApplicationContext(),"File uploaded successfully",Toast.LENGTH_LONG).show();


                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            generatedFilePath = downloadUri.toString();
                            List<String> myTeams;
                            myTeams = new ArrayList<String>();
                            myTeams.add("-1");
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            User u = new User(uid,etUserName.getText().toString(),etEmailLogin.getText().toString(),Integer.valueOf(etAge.getText().toString()),"",myTeams,generatedFilePath, spin.getSelectedItem().toString(),0,"no",deviceToken);
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Users").child(uid).setValue(u);

                            Intent intent = new Intent(RegisterActivity.this, ToBe.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
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

        if(etUserName.getText().length() < 1 || etEmailReg.getText().length() < 1 || etPassReg.getText().length() < 1 || etRePass.getText().length() < 1 || etAge.getText().length()< 1)
        {
            Toast.makeText(RegisterActivity.this, "Some Fields Are Empty. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
        else if(etPassReg.getText().toString().length() < 6)
        {
            Toast.makeText(RegisterActivity.this, "Password must be six Charcters or more. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!etPassReg.getText().toString().equals(etRePass.getText().toString()))
        {
            Toast.makeText(RegisterActivity.this, "Not same passwords. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!etEmailReg.getText().toString().matches(emailPattern))
        {
            Toast.makeText(RegisterActivity.this, "Invalid email. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
/*
        boolean checkUserName = false;
        for(int i=0; i <users.size(); i++)
        {
            if(users.get(i).equals(etUserName.getText()))
                checkUserName = true;
            Toast.makeText(RegisterActivity.this, users.get(i), Toast.LENGTH_LONG).show();
        }
        if(checkUserName == true)
        {
            Toast.makeText(RegisterActivity.this, "This Username used already, please choose another one", Toast.LENGTH_LONG).show();
            return;
        }
*/
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(etEmailReg.getText().toString(),etPassReg.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName( String.valueOf( etUserName.getText() ) )
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });

                    Toast.makeText(RegisterActivity.this, "Successfully registered. Welcome!", Toast.LENGTH_LONG).show();

                    //for user datails will save on data base
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    //Toast.makeText(RegisterActivity.this, uid,Toast.LENGTH_SHORT).show();
                    List<String> myTeams;
                    myTeams = new ArrayList<String>();
                    myTeams.add("-1");



                    uploadFile(uid);


                    if(b==true)
                    {
                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        User u = new User(uid,etUserName.getText().toString(),etEmailReg.getText().toString(),Integer.valueOf(etAge.getText().toString()),"",myTeams,generatedFilePath, spin.getSelectedItem().toString(),0,"no",deviceToken);
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("Users").child(uid).setValue(u);

                        Intent intent = new Intent(RegisterActivity.this, ToBe.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                } else {
                    Toast.makeText(RegisterActivity.this, "This email is already used. Please try again", Toast.LENGTH_LONG).show();
                }

                d.dismiss();
                progressDialog.dismiss();


            }
        });


    }

    public void login()
    {
        if(etEmailLogin.getText().length() < 1 || etEmailLogin.getText().length() < 1)
        {
            Toast.makeText(RegisterActivity.this, "Some Fields Are Empty. Please try again", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(etEmailLogin.getText().toString(),etPassLogin.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            String current_user_id =firebaseAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            mUserDatabase.child( current_user_id ).child( "device_token" ).setValue( deviceToken ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //.makeText(RegisterActivity.this, "auth success",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, ToBe.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } );



                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Failed! please try again",Toast.LENGTH_SHORT).show();

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


    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
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
        else if(v==cvLogIn)
        {
            login();
        }
        else if (btnReg==v)
        {
            register();
        }
        else if(v==btnChoose)
        {
            showFileChooser();
        }
        else if(v == btnfacebook)
        {
            Toast.makeText(this, "Our page will be able soon ", Toast.LENGTH_LONG).show();
            /*
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
            */
        }

    }

}