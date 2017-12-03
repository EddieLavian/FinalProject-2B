package com.example.talyeh3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by talyeh3 on 16/11/2017.
 */

public class OpenTeamAdapter extends ArrayAdapter<User> {
    Context context;
    List<User> objects;


    public OpenTeamAdapter(Context context, int resource, int textViewResourceId, List<User> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_user, parent, false);

        TextView tvUserName = (TextView)view.findViewById(R.id.tvUserName);
        User temp = objects.get(position);
        tvUserName.setText(temp.userName);



        return view;
    }




}
