package com.example.affinityDemo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Gareth on 26/12/2014.
 * The part of the listview that has details on a particular task
 */
public class ListItem implements CentralActivity.Item {

    Task task; // contains a task

    public ListItem (Task task)
    {
        this.task = task;
    }

    public Task getListTask()
    {
        return task;
    }

    @Override
    public int getViewType() {
        return outdatedTaskAdapter.RowType.LISTITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        View view;

        if(convertView == null)
        {
            view = (View) inflater.inflate(R.layout.taskrow,null); // inflate the view
        }
        else
        {
            view = convertView; // set if necessary
        }

        TextView taskName = (TextView) view.findViewById(R.id.taskName); // build the  layout
        TextView taskID = (TextView) view.findViewById(R.id.taskID);
        TextView assigneeEmail = (TextView) view.findViewById(R.id.assigneeEmail);

        taskName.setText("Task: " + task.getTaskTitle()); // set the text
        taskID.setText("Task ID: " + task.getTaskID());
        assigneeEmail.setText("Assignee: " + task.getAssigneeEmail());

        return view;
    }
}
