package com.example.affinityDemo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Gareth on 26/12/2014.
 *
 * The part of the list that holds the user's email to group elements by
 */
public class HeaderItem implements CentralActivity.Item {

    private String assigneeEmail;

    public HeaderItem(String assigneeEmail)
    {
        this.assigneeEmail = assigneeEmail;
    }

    public String getAssigneeEmail(){
        return assigneeEmail;
    }


    @Override
    public int getViewType() {
        return outdatedTaskAdapter.RowType.HEADER.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        View view;

        if(convertView == null)
        {
            view = (View) inflater.inflate(R.layout.listheader,null); // create the layout
        }
        else
        {
            view = convertView; // assign it if necessary
        }

        TextView sectionHeader = (TextView) view.findViewById(R.id.sectionHeader); // get a reference to the header

        sectionHeader.setText(assigneeEmail); // set the text

        return view;

    }
}
