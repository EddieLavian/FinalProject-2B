package com.tobe.talyeh3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AllUsersAdapter  extends ArrayAdapter<User> {
    Context context;
    List<User> objects;

    public AllUsersAdapter(Context context, int resource, int textViewResourceId, List<User> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_allusers, parent, false);

        TextView tvUserName = (TextView)view.findViewById(R.id.tvUserName);



        User temp = objects.get(position);
        if (tvUserName == null ||temp==null)
            return view;
        tvUserName.setText(temp.userName);

        ImageView imgProfile = (ImageView)view.findViewById(R.id.imgProfile);


            Picasso
                    .with( context )
                    .load( temp.imgUrl )
                    .fit() // will explain later
                    .into( imgProfile );



        return view;
    }

}
