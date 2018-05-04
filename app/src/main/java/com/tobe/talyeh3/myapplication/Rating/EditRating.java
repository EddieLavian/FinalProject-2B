package com.tobe.talyeh3.myapplication.Rating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tobe.talyeh3.myapplication.R;
import com.tobe.talyeh3.myapplication.Statistics.Statistics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditRating extends AppCompatActivity  implements View.OnClickListener{
    TextView tvName;
    Spinner spinner;
    Button btnSave;
    FirebaseDatabase database;
    DatabaseReference ratingRef;
    String key;
    Rating r;
    String myUserId;
    Boolean checkIfRated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_rating );

        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        tvName =(TextView)findViewById( R.id.tvName );
        spinner = (Spinner) findViewById(R.id.spinner);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        Intent intent = getIntent();
        key = intent.getExtras().getString("keyRating");
        ratingRef = database.getReference("Rating/" + key);
        //Toast.makeText(EditPostActivity.this, "user: " + postRef, Toast.LENGTH_LONG).show();
        this.retrieveData();

    }


    public void retrieveData()
    {
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                r = dataSnapshot.getValue( Rating.class );
                tvName.setText( r.name );
                //etRating.setText( String.valueOf( r.avgRating ) );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onClick(View v) {
        if (v==btnSave)
        {
            ratingRef = database.getReference("Rating/" + r.key);
            r.avgRating = Integer.valueOf( spinner.getSelectedItem().toString());
            if (r.rating.get( 0 ).equals( -1 ))
            {
                r.whoIsRating.set( 0,myUserId );
                r.rating.set(0, Integer.valueOf( spinner.getSelectedItem().toString() )   );
            }
            else
            {
                for (int i = 0; i<r.whoIsRating.size();i++)
                {
                    if(r.whoIsRating.get( i ).equals( myUserId ))
                    {
                        r.rating.set( i,Integer.valueOf( spinner.getSelectedItem().toString()) );
                        checkIfRated = true;
                    }
                }
                if (checkIfRated.equals( false ))
                {
                    r.whoIsRating.add( myUserId );
                    r.rating.add( Integer.valueOf( spinner.getSelectedItem().toString()) );
                }
            }
            int i;
            r.avgRating=0;
            for (i = 0 ; i < r.rating.size();i++)
            {
                r.avgRating=r.avgRating+ r.rating.get( i );
            }
            r.avgRating=r.avgRating/(i);
            ratingRef.setValue(r);
            finish();

        }
    }
}
