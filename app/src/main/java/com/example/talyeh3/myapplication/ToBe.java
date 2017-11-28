package com.example.talyeh3.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ToBe extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    Button btnAddPost;
    Button btnReg,btnLogin;//dialog buttons
    Button btnMainLogin,btnMainRegister;
    EditText etEmail,etPass;
    Dialog d;
    int mode=0; // o means register 1 means login
    ProgressDialog progressDialog;
    Button btnAllPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be);

        firebaseAuth = FirebaseAuth.getInstance();
        btnMainLogin = (Button)findViewById(R.id.btnLogin);
        btnMainLogin.setOnClickListener(this);
        btnAllPost = (Button)findViewById(R.id.btnAllPost);

        btnMainRegister = (Button)findViewById(R.id.btnRegister);
        btnMainRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            btnMainLogin.setText("Logout");

        }
        else
        {
            btnMainLogin.setText("Login");

        }
        btnAddPost = (Button)findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,AddPostActivity.class);
                startActivity(intent);
            }
        });

        btnAllPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToBe.this,AllPostActivity.class);
                startActivity(intent);
            }
        });


    }


    public void createRegisterDialog()
    {
        d= new Dialog(this);
        d.setContentView(R.layout.registerlayout);
        d.setTitle("Register");
        d.setCancelable(true);
        etEmail=(EditText)d.findViewById(R.id.etEmail);
        etPass=(EditText)d.findViewById(R.id.etPass);
        btnReg=(Button)d.findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(this);
        d.show();

    }
    public void createLoginDialog()
    {
        d= new Dialog(this);
        d.setContentView(R.layout.login_layout);
        d.setTitle("Login");
        d.setCancelable(true);
        etEmail=(EditText)d.findViewById(R.id.etEmail);
        etPass=(EditText)d.findViewById(R.id.etPass);
        btnLogin=(Button)d.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        d.show();

    }

    public void register()
    {

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ToBe.this, "Successfully registered", Toast.LENGTH_LONG).show();
                    btnMainLogin.setText("Logout");

                } else {
                    Toast.makeText(ToBe.this, "Registration Error", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(ToBe.this, "auth_success",Toast.LENGTH_SHORT).show();
                            btnMainLogin.setText("Logout");

                        }
                        else
                        {
                            Toast.makeText(ToBe.this, "auth_failed",Toast.LENGTH_SHORT).show();

                        }
                        d.dismiss();
                        progressDialog.dismiss();

                    }
                });

    }

    @Override
    public void onClick(View v) {

        if(v==btnMainLogin)
        {

            if(btnMainLogin.getText().toString().equals("Login"))
            {
                createLoginDialog();
            }
            else if(btnMainLogin.getText().toString().equals("Logout"))
            {
                firebaseAuth.signOut();
                btnMainLogin.setText("Login");
            }

        }
        else if(v==btnMainRegister)
        {
            createRegisterDialog();
        }
        else if (btnReg==v)
        {
            register();
        }
        else if(v==btnLogin)
        {
            login();
        }


    }
}





