package com.example.talyeh3.myapplication.CreateGame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.talyeh3.myapplication.Game;
import com.example.talyeh3.myapplication.R;
import java.util.List;

/**
 * Created by talyeh3 on 18/12/2017.
 */

public class TeamGamesAdapter extends ArrayAdapter<Game> {
    Context context;
    List<Game> objects;
    Boolean ifAttending,check=true;
    String myUserId;

    public TeamGamesAdapter(Context context, int resource, int textViewResourceId, List<Game> objects,Boolean ifAttending,String myUserId) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.ifAttending=ifAttending;
        this.myUserId= myUserId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_game, parent, false);
        TextView tvDate = (TextView)view.findViewById(R.id.tvDate);
        TextView tvAttending = (TextView)view.findViewById(R.id.tvAttending);
        Game temp = objects.get(position);
        tvDate.setText(temp.date);

        for (int i = 0 ; i<temp.whoIsComming.size();i++)
        {
            if(temp.whoIsComming.get( i ).equals( myUserId ))
            {
                tvAttending.setText( "Arrive To Next Game" );
                tvAttending.setTextColor( Color.GREEN);

            }
        }


        return view;
    }
}
