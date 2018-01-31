package com.example.talyeh3.myapplication.CreateGame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.AllUsersAdapter;
import com.example.talyeh3.myapplication.Chat.ChatActivity;
import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Team.OpenTeam;
import com.example.talyeh3.myapplication.Team.ProfileActivity;
import com.example.talyeh3.myapplication.Team.TeamDetails;
import com.example.talyeh3.myapplication.ToBeTest;
import com.example.talyeh3.myapplication.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by talyeh3 on 18/12/2017.
 */

public class TeamGamesAdapter extends ArrayAdapter<Game> {
    Context context;
    List<Game> objects;
    Boolean ifAttending,check=true;
    String myUserId, playersAttended;
    Dialog dialog;
    private DatabaseReference database,teamDatabase;;
    ArrayList<User> users;
    String keyUser;
    int i;
    ListView lv;
    AllUsersAdapter allPlayersAdapter;
    Game temp;
    String keyGame;

    public TeamGamesAdapter(Context context, int resource, int textViewResourceId, List<Game> objects,Boolean ifAttending,String myUserId,Dialog dialog,String keyGame) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.ifAttending=ifAttending;
        this.myUserId= myUserId;
        this.playersAttended="Attended: ";
        this.dialog=dialog;
        this.i=1;
        this.keyGame=keyGame;



    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_game, parent, false);
        TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
        TextView tvAttending = (TextView)view.findViewById(R.id.tvAttending);
        final TextView tvPlayers = (TextView)view.findViewById(R.id.tvPlayers);
        TextView tvTime = (TextView)view.findViewById(R.id.tvTime);
        TextView tvPlace = (TextView)view.findViewById(R.id.tvPlace);
        TextView tvAttended = (TextView)view.findViewById(R.id.tvAttended);
        TextView tvMinimumPlayers = (TextView)view.findViewById(R.id.tvMinimumPlayers);

        temp = objects.get(position);


        tvDate.setText(temp.date);
        tvTime.setText( temp.location );
        tvTime.setText( temp.time );
        tvPlace.setText( temp.location );
        tvAttended.setText( "Attended: "+String.valueOf(temp.attending  ) );

        tvMinimumPlayers.setText("Minimum players: "+ String.valueOf(temp.minimumPlayers ));



        for (int i = 1 ; i<temp.whoIsComming.size();i++)
        {
            if(temp.whoIsComming.get( i ).equals( myUserId ))
            {
                tvAttending.setText( "Arrive To Next Game" );
                tvAttending.setTextColor( Color.GREEN);

            }
        }
        for (int i = 1 ; i<temp.whoIsComming.size();i++)
        {
                playersAttended= playersAttended + temp.whoIsComming.get( i );
        }





        tvPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance().getReference("Games/"+keyGame+"/whoIsComming");
               // Toast.makeText(context, tvPlayers.getParent().toString(),Toast.LENGTH_SHORT).show();
                retriveDataPlayers();
                dialog.setContentView( R.layout.activity_all_users);
                dialog.setCancelable(true);
                lv = (ListView) dialog.findViewById( R.id.lv);

                dialog.show();
            }
        });







        return view;
    }
    public void retriveDataPlayers() {
        database.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                users = new ArrayList<User>();
                i=1;
                for (DataSnapshot data : snapshot.getChildren()) {
                    keyUser = (String) snapshot.child(String.valueOf(i)).getValue();//put in array of users at the teams key and not mail
                      //   Toast.makeText(context, String.valueOf(i),Toast.LENGTH_SHORT).show();
                    if (keyUser!=null &&keyUser!="-1")
                    {
                        teamDatabase = FirebaseDatabase.getInstance().getReference("Users/" + keyUser);
                        ValueEventListener valueEventListener = teamDatabase.addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {
                                User u = snapshot.getValue(User.class);
                                users.add(u);
                                snapshot.toString();
                                allPlayersAdapter.notifyDataSetChanged();
                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                        allPlayersAdapter = new AllUsersAdapter(context, 0, 0, users);
                        lv.setAdapter(allPlayersAdapter);
                   }
                    i++;
                }
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
