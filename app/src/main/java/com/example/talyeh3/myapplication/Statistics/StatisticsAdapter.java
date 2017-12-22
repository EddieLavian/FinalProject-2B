package com.example.talyeh3.myapplication.Statistics;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.R;

import java.util.List;

/**
 * Created by talyeh3 on 22/12/2017.
 */

public class StatisticsAdapter extends ArrayAdapter<Statistics> {
    Context context;
    List<Statistics> objects;

    public StatisticsAdapter(Context context, int resource, int textViewResourceId, List<Statistics> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.custom_statistics, parent, false);
       TextView tvName = (TextView)view.findViewById(R.id.tvName);
        TextView tvGames = (TextView)view.findViewById(R.id.tvGames);
        TextView tvGoals = (TextView)view.findViewById(R.id.tvGoals);
        TextView tvAssists = (TextView)view.findViewById(R.id.tvAssists);
        Statistics temp = objects.get(position);
        tvName.setText(temp.name);
        tvGames.setText(String.valueOf(temp.games));
        tvGoals.setText(String.valueOf(temp.goals));
        tvAssists.setText(String.valueOf(temp.assist));
        return view;

    }


}
