package com.example.talyeh3.myapplication.Team;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talyeh3.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by talyeh3 on 19/11/2017.
 */

public class MyTeamsAdapter extends ArrayAdapter<Team> {
    Context context;
    List<Team> objects;

    public MyTeamsAdapter(Context context, int resource, int textViewResourceId, List<Team> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_team, parent, false);
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        // String  temp = objects.get(position);
        //tvTitle.setText(temp);
        Team temp = objects.get(position);
        tvTitle.setText(temp.name);

        ImageView imgTeamProfile = (ImageView)view.findViewById(R.id.imgTeamProfile);


        Picasso
                .with( context )
                .load( temp.imgUrl )
                .fit() // will explain later
                .into( imgTeamProfile );


        return view;
    }

}
