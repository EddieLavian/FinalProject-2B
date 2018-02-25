package com.example.talyeh3.myapplication.Rating;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForcesAdapter extends ArrayAdapter<Rating> {
    Context context;
    List<Rating> objects;

    public ForcesAdapter(Context context, int resource, int textViewResourceId, List<Rating> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_force, parent, false);

        TextView tvUserName = (TextView)view.findViewById(R.id.tvUserName);



        Rating temp = objects.get(position);
        tvUserName.setText(temp.name);

        ImageView imgProfile = (ImageView)view.findViewById(R.id.imgProfile);


            Picasso
                    .with( context )
                    .load( temp.imgUrl )
                    .fit() // will explain later
                    .into( imgProfile );



        return view;
    }

}
