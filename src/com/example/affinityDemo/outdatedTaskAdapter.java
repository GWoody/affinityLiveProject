package com.example.affinityDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;


/**
 * Created by Gareth on 22/12/2014.
 * The adapter that empties Task data into the Listview
 */
public class outdatedTaskAdapter extends ArrayAdapter<CentralActivity.Item> {

    private LayoutInflater inflater; // hold an inflater


    public enum RowType{ // enum to determine what rowtype we have
        LISTITEM, HEADER
    }


    public outdatedTaskAdapter(Context context, ArrayList<CentralActivity.Item> tasks) {
        super(context,0,tasks);
        inflater = LayoutInflater.from(context); // get the context for the inflater
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType(); // get the type of item we are currently pointing at
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       return getItem(position).getView(inflater,convertView); // return a refernce to the getView of ListItem or HeaderItem


    }


}
