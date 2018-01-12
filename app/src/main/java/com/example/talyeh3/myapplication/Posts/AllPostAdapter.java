package com.example.talyeh3.myapplication.Posts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talyeh3.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by talyeh3 on 16/11/2017.
 */

public class AllPostAdapter extends ArrayAdapter<Post> {

    Context context;
    List<Post> objects;

    public AllPostAdapter(Context context, int resource, int textViewResourceId, List<Post> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_post, parent, false);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy, hh:mm a");
        String dateString = sdf.format(date);

        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setTypeface(null, Typeface.BOLD);
        Post temp = objects.get(position);
        tvTitle.setText(temp.title + "\n" + dateString);
        ImageView imgProfile = (ImageView)view.findViewById(R.id.imgProfile);


        Picasso
                .with( context )
                .load( temp.imgUrl )
                .fit() // will explain later
                .into( imgProfile );


        return view;
    }
}
