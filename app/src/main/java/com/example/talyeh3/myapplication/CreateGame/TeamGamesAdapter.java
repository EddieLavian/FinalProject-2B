package com.example.talyeh3.myapplication.CreateGame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.talyeh3.myapplication.AllUsersAdapter;
import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.User;
import com.google.firebase.database.DatabaseReference;

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
    ListView listView;
    AllUsersAdapter allPlayersAdapter;

    customButtonListener customListner;
    public interface customButtonListener {
        public void onButtonClickListner(int position, Game value);
        public void onButtonClickListner2(int position, Game value);
        public void onButtonForces(int position, Game value);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    String keyGame;

    public TeamGamesAdapter(Context context, int resource, int textViewResourceId, List<Game> objects,Boolean ifAttending,String myUserId,Dialog dialog,String keyGame) {
        super(context, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.ifAttending=ifAttending;
        this.myUserId= myUserId;
        this.playersAttended="Attended: ";
        this.dialog=dialog;
        this.i=1;
        this.keyGame=keyGame;
    }

    static class ViewHolder {
        TextView tvDate,tvAttending,tvTime,tvPlace,tvAttended,tvMinimumPlayers;
        Button tvPlayers,tvAcceptArrive,btnForce;
    }

    public View getView( final int position, View convertView, ViewGroup parent) {
        final Game temp = getItem(position);
        View rowView=convertView;
        if (rowView == null) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
         rowView = layoutInflater.inflate( R.layout.custom_game, parent, false);

        ViewHolder h = new ViewHolder();
         h.tvDate = (TextView)rowView.findViewById(R.id.tvDate);
            h.tvAttending = (TextView)rowView.findViewById(R.id.tvAttending);
            h.tvPlayers = (Button)rowView.findViewById(R.id.tvPlayers);
            h.btnForce = (Button)rowView.findViewById(R.id.btnForce);
            h.tvTime = (TextView)rowView.findViewById(R.id.tvTime);
            h.tvPlace = (TextView)rowView.findViewById(R.id.tvPlace);
            h.tvAttended = (TextView)rowView.findViewById(R.id.tvAttended);
            h.tvMinimumPlayers = (TextView)rowView.findViewById(R.id.tvMinimumPlayers);
            h.tvAcceptArrive= (Button)rowView.findViewById(R.id.tvAcceptArrive);
            rowView.setTag(h);
        }

        ViewHolder h = (ViewHolder) rowView.getTag();
        h.tvDate.setText(temp.date);
        h.tvTime.setText( temp.location );
        h.tvTime.setText( temp.time );
        h.tvPlace.setText( temp.location );
        h.tvAttended.setText( "Attended: "+String.valueOf(temp.attending  ) );

        h.tvMinimumPlayers.setText("Minimum players: "+ String.valueOf(temp.minimumPlayers ));


        Boolean b = false;
        for (int i = 1 ; i<temp.whoIsComming.size();i++)
        {
            if(temp.whoIsComming.get( i ).equals( myUserId ))
            {
                b=true;
                h.tvAttending.setText( "Arrive To Next Game" );
                h.tvAcceptArrive.setText( "Cancel Arrive" );
                h.tvAttending.setTextColor( Color.GREEN);

            }
        }
        if (b==false)
        {
            h.tvAttending.setText( "not Arrive To Next Game" );
            h.tvAcceptArrive.setText( "Accept Arrive" );
            h.tvAttending.setTextColor( Color.BLACK);
        }

        for (int i = 1 ; i<temp.whoIsComming.size();i++)
        {
                playersAttended= playersAttended + temp.whoIsComming.get( i );
        }

        h.tvPlayers.setOnClickListener(new View.OnClickListener() {//who is comming

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position,temp);
                }


            }
        });
        h.tvAcceptArrive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner2(position,temp);
                }

            }
        });
        h.btnForce.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonForces(position,temp);
                }

            }
        });


        return rowView;
    }
}
