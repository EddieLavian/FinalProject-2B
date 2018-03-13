package com.example.talyeh3.myapplication.Gallery;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.talyeh3.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class GalleryActivity extends AppCompatActivity {

    private TextView tvName;
    private RecyclerView rvMensajes;
    private AdapterGallery adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int PHOTO_SEND = 1;
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
        setContentView( R.layout.activity_gallery );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvName = (TextView) findViewById(R.id.tvName);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);

        initCollapsingToolbar();
        Intent intent = getIntent();
        if( intent.getExtras().getString("teamKey") != null)
        {
            key = intent.getExtras().getString("teamKey");
            name = intent.getExtras().getString("teamName");
            profilePic = intent.getExtras().getString("profilePic");
            userName = intent.getExtras().getString("userName");
            //Toast.makeText( GalleryActivity.this,"kkkkk"+ profilePic,Toast.LENGTH_SHORT).show();
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("Gallery/"+key);//Sala de chat (nombre)

            getUsersInTeam();
            mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child( "notifications" );
            myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        }
        else
        {
            profilePic = intent.getExtras().getString("profilePic");
            userName = intent.getExtras().getString("userName");
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("chat/GeneralChat");//Sala de chat (nombre)

        }
        storage = FirebaseStorage.getInstance();

        adapter = new AdapterGallery(this);
       // LinearLayoutManager l = new LinearLayoutManager(this);
        RecyclerView.LayoutManager l = new GridLayoutManager(this, 2);
        rvMensajes.setLayoutManager(l);
        rvMensajes.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvMensajes.setItemAnimator(new DefaultItemAnimator());
        rvMensajes.setAdapter(adapter);


        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_SEND);
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


    public void getUsersInTeam()
    {
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
    }
    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
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
                    MensajeEnviar m = new MensajeEnviar("",u.toString(),userName+" Uploded Photo",profilePic,"2",ServerValue.TIMESTAMP);
                    databaseReference.push().setValue(m);
                }
            });


            if (usersInTeam!=null)
            {
                for (int i = 0 ; i < usersInTeam.size(); i++) {
                    //  if (!myUserId.equals(  usersInTeam.get( i )))
                    {
                        HashMap<String, String> notificationData = new HashMap<>();
                        notificationData.put( "from", myUserId );
                        notificationData.put( "type", "Upload a new photo to team "+name );
                        mNotificationDatabase.child( usersInTeam.get( i ) ).push().setValue( notificationData ).addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        } );
                    }
                }
            }


        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round( TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
