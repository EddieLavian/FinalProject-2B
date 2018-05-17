package com.tobe.talyeh3.myapplication.Team;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tobe.talyeh3.myapplication.AllUsersAdapter;
import com.tobe.talyeh3.myapplication.R;
import com.tobe.talyeh3.myapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class OpenTeam extends AppCompatActivity {
    MaterialSearchView searchView;
    ListView lstView;

    String teamKey;
    TextView tvTitle;
    ListView lv;
    ArrayList<User> users;
    AllUsersAdapter allUsersAdapter;
    TextView tvAddPlayer;
    ImageView image;
    private DatabaseReference database,userRefTeam,perRefTeam;
    ArrayList<String> usersInTeam,perInTeam;
    String myUserId;
    Button btn;
    String keyUser="";
    ProgressDialog progressDialog;
    int i = 0;
    String delete;
    String permissions;
   public boolean autocompliteUse;
    List<User> lstFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_open_team);

       // Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Material Search");
       // toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        autocompliteUse=false;
        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference("Users");
        lv = (ListView) findViewById( R.id.lv);
        Intent getIntent = getIntent();
        teamKey = getIntent.getExtras().getString("teamKey");
        delete = getIntent.getExtras().getString("delete");
        permissions = getIntent.getExtras().getString("permissions");

        userRefTeam = FirebaseDatabase.getInstance().getReference("Teams/"+teamKey+"/users");
        perRefTeam = FirebaseDatabase.getInstance().getReference("Teams/"+teamKey+"/permissions");
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        if (delete!=null)
        {
            tvTitle.setText( "Choose Player To Delete " );
        }
        else if(permissions != null)
        {
            tvTitle.setText( "Give Permissions " );
        }
        this.retriveData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u;
                if (autocompliteUse==false)
                {
                    u = users.get(position);
                }
                else
                {
                    u = lstFound.get( position );
                }

                Intent intent = new Intent(OpenTeam.this, ProfileActivity.class);
                intent.putExtra("key", u.uid );
                intent.putExtra("team",teamKey);
                intent.putExtra("photo", u.imgUrl );
                if(delete!=null)
                    intent.putExtra("delete", "del");
                else if(permissions != null)
                    intent.putExtra("permissions", "per");
                startActivity(intent);

            }

        });


    }

    public void retriveData() {
        progressDialog.setMessage("load Please Wait...");
        progressDialog.show();




        perRefTeam.addValueEventListener(new ValueEventListener() {


            public void onDataChange(DataSnapshot dataSnapshot) {
                perInTeam = new ArrayList<String>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String u = String.valueOf( data.getValue( ) );

                    //Toast.makeText( OpenTeam.this, "sd  " + u, Toast.LENGTH_SHORT ).show();
                    perInTeam.add( u );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        userRefTeam.addValueEventListener(new ValueEventListener() {


            public void onDataChange(DataSnapshot dataSnapshot) {
                usersInTeam = new ArrayList<String>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String u = String.valueOf( data.getValue( ) );

                    //Toast.makeText( OpenTeam.this, "sd  " + u, Toast.LENGTH_SHORT ).show();
                    usersInTeam.add( u );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<User>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User u = data.getValue(User.class);
                    int notDuplicateUser=0;

                    if (permissions!=null)
                    {

                        for (int i = 0; i < usersInTeam.size(); i++) {
                            if (usersInTeam.get(i).equals(u.uid) && !usersInTeam.get(i).equals(myUserId)) {//in the team but not me
                                users.add(u);
                                break;
                            }
                        }

                        for (int i = 0; i < perInTeam.size(); i++)
                        {
                            if (perInTeam.get( i ).equals( u.uid )&&!perInTeam.get( i ).equals(myUserId))
                            {//in the team but not me
                                users.remove(u);
                                break;
                            }
                        }
                    }

                    else if (delete==null){ //add players list
                        for (int i = 0; i < usersInTeam.size(); i++) {
                            if (usersInTeam.get( i ).equals( u.uid )) {
                                notDuplicateUser = -1;
                            }
                        }
                        if (notDuplicateUser==0 )
                            users.add(u);
                    }
                    else if(delete != null)//delete players list
                    {
                        for (int i = 0; i < usersInTeam.size(); i++) {
                            if (usersInTeam.get( i ).equals( u.uid )&&!usersInTeam.get( i ).equals(myUserId))
                            {//in the team but not me
                                users.add(u);
                                break;
                            }
                        }
                    }
                }
                progressDialog.dismiss();
              // allUsersAdapter = new AllUsersAdapter(OpenTeam.this, 0, 0, users);
              //  lv.setAdapter(allUsersAdapter);

                lstView = (ListView)findViewById(R.id.lv);
                allUsersAdapter = new AllUsersAdapter(OpenTeam.this, 0, 0, users);
                lstView.setAdapter(allUsersAdapter);


                searchView = (MaterialSearchView)findViewById(R.id.search_view);

                searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

                    public void onSearchViewShown() {

                    }


                    public void onSearchViewClosed() {

                        //If closed Search View , lstView will return default
                        lstView = (ListView)findViewById(R.id.lv);
                         //ArrayAdapter adapter = new ArrayAdapter(OpenTeam.this,android.R.laMinimumt.simple_list_item_1,lstSource);
                        allUsersAdapter = new AllUsersAdapter(OpenTeam.this, 0, 0, users);
                         lstView.setAdapter(allUsersAdapter);


                    }
                });

                searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }


                    public boolean onQueryTextChange(String newText) {
                        if(newText != null && !newText.isEmpty()){
                            lstFound = new ArrayList<User>();
                            int i=0;
                            for(User item:users ){
                                    if(item.userName.contains( newText ) )
                                    {
                                        lstFound.add(item);
                                        autocompliteUse =true;
                                    }
                            }

                            allUsersAdapter = new AllUsersAdapter(OpenTeam.this, 0, 0, lstFound);
                            lstView.setAdapter(allUsersAdapter);
                        }
                        else{
                            //if search text is null
                            //return default
                            allUsersAdapter = new AllUsersAdapter(OpenTeam.this, 0, 0, users);
                            lstView.setAdapter(allUsersAdapter);
                        }
                        return true;
                    }

                });

                allUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}