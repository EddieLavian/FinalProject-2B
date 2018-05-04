package com.tobe.talyeh3.myapplication.Rating;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tobe.talyeh3.myapplication.R;


import java.util.List;

/**
 * Created by talyeh3 on 22/12/2017.
 */

public class RatingAdapter extends ArrayAdapter<Rating> {
    Context context;
    List<Rating> objects;
    String myUserId;

    public RatingAdapter(Context context, int resource, int textViewResourceId, List<Rating> objects,String myUserId) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
        this.myUserId= myUserId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_rating, parent, false);
       TextView tvName = (TextView)view.findViewById(R.id.tvName);
        TextView tvRating = (TextView)view.findViewById(R.id.tvRating);
        TextView tvMyRate = (TextView)view.findViewById(R.id.tvMyRate);

        Rating temp = objects.get(position);
        tvName.setText(temp.name);
        Boolean check = false;
        for (int i = 0 ; i < temp.whoIsRating.size(); i ++)
        {
            if (temp.whoIsRating.get( i ).equals( myUserId ))
            {
                tvMyRate.setText(String.valueOf(temp.rating.get( i )));
                check = true;
            }
        }
        if (check.equals( false ))
        {
            tvMyRate.setText("need to rate");
        }


        tvRating.setText(String.valueOf(temp.avgRating));

        return view;

    }


}
