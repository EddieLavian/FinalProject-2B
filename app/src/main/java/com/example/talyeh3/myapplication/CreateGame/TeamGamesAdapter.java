package com.example.talyeh3.myapplication.CreateGame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Team.OpenTeam;
import com.example.talyeh3.myapplication.Team.ProfileActivity;
import com.example.talyeh3.myapplication.User;

import java.util.List;

/**
 * Created by talyeh3 on 18/12/2017.
 */

public class TeamGamesAdapter extends ArrayAdapter<Game> {
    Context context;
    List<Game> objects;
    Boolean ifAttending,check=true;
    String myUserId, playersAttended;


    public TeamGamesAdapter(Context context, int resource, int textViewResourceId, List<Game> objects,Boolean ifAttending,String myUserId) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.ifAttending=ifAttending;
        this.myUserId= myUserId;
        this.playersAttended="Attended: ";
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_game, parent, false);
        TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
        TextView tvAttending = (TextView)view.findViewById(R.id.tvAttending);
        TextView tvPlayers = (TextView)view.findViewById(R.id.tvPlayers);
        TextView tvTime = (TextView)view.findViewById(R.id.tvTime);
        TextView tvPlace = (TextView)view.findViewById(R.id.tvPlace);
        TextView tvAttended = (TextView)view.findViewById(R.id.tvAttended);
        TextView tvMinimumPlayers = (TextView)view.findViewById(R.id.tvMinimumPlayers);

        Game temp = objects.get(position);
        tvDate.setText(temp.date);
        tvTime.setText( temp.location );
        tvTime.setText( temp.time );
        tvPlace.setText( temp.location );
        tvAttended.setText( "Attended: "+String.valueOf(temp.attending  ) );

        tvMinimumPlayers.setText("Minimum players: "+ String.valueOf(temp.minimumPlayers ));



        for (int i = 0 ; i<temp.whoIsComming.size();i++)
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

        tvPlayers.setText(playersAttended);
        return view;
    }
}
