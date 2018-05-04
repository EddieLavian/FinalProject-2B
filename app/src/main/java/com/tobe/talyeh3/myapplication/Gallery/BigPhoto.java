package com.tobe.talyeh3.myapplication.Gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tobe.talyeh3.myapplication.Chat.ChatActivity;
import com.tobe.talyeh3.myapplication.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BigPhoto extends AppCompatActivity {
    String pic="";
    ImageView imgPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_big_photo );
        imgPic = (ImageView) findViewById(R.id.imgPic);
        Intent intent = getIntent();

        if( intent.getExtras().getString("pic") != null)
        {

            pic = intent.getExtras().getString("pic");

            Picasso
                    .with( BigPhoto.this )
                    .load( pic )
                    .fit() // will explain later
                    .into( imgPic );

        }
    }
}
