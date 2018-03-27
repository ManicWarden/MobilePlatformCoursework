package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;



public class CurrentIncidentsAdapter extends ArrayAdapter<CurrentIncidentsItems> {

    private Context context;
    private ArrayList<CurrentIncidentsItems> itemsArrayList;
    private int resourceID;
    private static LayoutInflater inflater = null;

    public CurrentIncidentsAdapter (Context context, int listViewID, ArrayList<CurrentIncidentsItems> _items)
    {
        super(context, listViewID, _items);
        // setting the activity and array list to the parameters passed to the class
        // therefore activity will be Current Incidents and itemsArrayList will be the list of objects

        this.resourceID = listViewID;
        this.context = context;
        this.itemsArrayList = _items;
    }

    public int getCount()
    {
        return itemsArrayList.size();
    }

    /*@Override
    public HashMap<String, String> getItem(int position)
    {
        return itemsArrayList[position];
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        CurrentIncidentsItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);

            holder = new CurrentIncidentsItemHolder();
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.description = (TextView) row.findViewById(R.id.description);
            holder.location = (TextView) row.findViewById(R.id.location);
            holder.duration = (TextView) row.findViewById(R.id.Duration);
            row.setTag(holder);
        }
        else
        {
            holder = (CurrentIncidentsItemHolder) row.getTag();
        }

        CurrentIncidentsItems item = itemsArrayList.get(position);
        holder.title.setText(item.get_title());
        holder.description.setText(item.get_description());
        holder.location.setText(item.get_location());
        holder.duration.setText(item.get_duration());
        // hiding the unnecessary text views

        holder.description.setVisibility(View.GONE);
        holder.location.setVisibility(View.GONE);
        holder.duration.setVisibility(View.GONE);

        int duration = Integer.parseInt(item.get_duration());


        if(duration >= 0 && duration <= 10)
        {
            row.setBackgroundColor(Color.parseColor("#ffff00"));
        }
        else if(duration > 10 && duration <= 20)
        {
            row.setBackgroundColor(Color.parseColor("#ffd700"));
        }
        else if(duration > 20 && duration <= 40)
        {
            row.setBackgroundColor(Color.parseColor("#ffa500"));
        }
        else if(duration > 40 && duration <= 70)
        {
            row.setBackgroundColor(Color.parseColor("#ff8c00"));
        }
        else if(duration > 70 && duration <= 100)
        {
            row.setBackgroundColor(Color.parseColor("#ff4500"));
        }
        else
        {
            row.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        return row;

    }

    private class CurrentIncidentsItemHolder {
        public TextView title;
        public TextView description;
        public TextView location;
        public TextView duration;

    }

}
