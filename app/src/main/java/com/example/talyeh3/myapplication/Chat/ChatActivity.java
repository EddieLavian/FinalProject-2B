package com.example.talyeh3.myapplication.Chat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.talyeh3.myapplication.Posts.AllPostActivity;
import com.example.talyeh3.myapplication.Posts.AllPostAdapter;
import com.example.talyeh3.myapplication.Posts.Post;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.RegisterActivity;
import com.example.talyeh3.myapplication.ToBeTest;
import com.example.talyeh3.myapplication.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView tvName;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private String fotoPerfilCadena;
    String key="General Chat";
    String name="General Chat";
    String profilePic="",userName="";

    String myUserId;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mNotificationDatabase;
    ArrayList<String > usersInTeam;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );
        getSupportActionBar().hide();

        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child( "notifications" );
        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        tvName = (TextView) findViewById(R.id.tvName);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";
        Intent intent = getIntent();
        if( intent.getExtras().getString("teamKey") != null)
        {

            key = intent.getExtras().getString("teamKey");
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child( "Teams/"+key+"/users/" );

            mUserDatabase.addValueEventListener(new ValueEventListener() {//users in team
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    usersInTeam = new ArrayList<String>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String s = String.valueOf( data.getValue() );
                        usersInTeam.add(s);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            name = intent.getExtras().getString("teamName");
            profilePic = intent.getExtras().getString("profilePic");
            userName = intent.getExtras().getString("userName");
           // Toast.makeText(ChatActivity.this,"kkkkk"+ profilePic,Toast.LENGTH_SHORT).show();
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("chat/"+key);//Sala de chat (nombre)

            tvName.setText(name);

        }
        else
        {
            profilePic = intent.getExtras().getString("profilePic");
            userName = intent.getExtras().getString("userName");
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("chat/GeneralChat");//Sala de chat (nombre)
            //Intent intentName = getIntent();//for name of user

        }




        storage = FirebaseStorage.getInstance();

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.push().setValue(new MensajeEnviar(txtMensaje.getText().toString(),userName,profilePic,"1", ServerValue.TIMESTAMP));
                txtMensaje.setText("");


            if (usersInTeam!=null)
            {
                for (int i = 0 ; i < usersInTeam.size(); i++) {
                  //  if (!myUserId.equals(  usersInTeam.get( i )))
                    {
                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put( "from", myUserId );
                    notificationData.put( "type", "Sent you a message in team "+name );
                    mNotificationDatabase.child( usersInTeam.get( i ) ).push().setValue( notificationData ).addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    } );
                }
                }
            }

            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_SEND);
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_PERFIL);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m,profilePic);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    MensajeEnviar m = new MensajeEnviar("",u.toString(),userName+" Uploaded photo",profilePic,"2",ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                }
            });
        }else if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("foto_perfil");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    fotoPerfilCadena = u.toString();
                    MensajeEnviar m = new MensajeEnviar("",u.toString(),userName+" changed team photo",profilePic,"2",ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                    Glide.with(ChatActivity.this).load(u.toString()).into(fotoPerfil);
                }
            });
        }
    }
}
