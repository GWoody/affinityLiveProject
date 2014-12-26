package com.example.affinityDemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gareth on 22/12/2014.
 * Contains the Task information that is retrieved from the query
 */
public class Task implements Parcelable{

    private String taskID;
    private String taskTitle;
    private String assigneeID;
    private String assigneeEmail;
    private String assigneeFirstname;
    private String assigneeSurname;

    public Task() {
        taskID = "";
        taskTitle = "";
        assigneeID = "";
        assigneeEmail = "";
        assigneeFirstname = "";
        assigneeSurname = "";
    }

    public Task(Parcel in)
    {
        this.taskID = in.readString();
        this.taskTitle = in.readString();
        this.assigneeID = in.readString();
        this.assigneeEmail = in.readString();
        this.assigneeFirstname = in.readString();
        this.assigneeSurname = in.readString();
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public void setAssigneeID(String assigneeID) {
        this.assigneeID = assigneeID;
    }

    public void setAssigneeFirstname(String assigneeFirstname) {
        this.assigneeFirstname = assigneeFirstname;
    }

    public void setAssigneeSurname(String assigneeSurname) {
        this.assigneeSurname = assigneeSurname;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getAssigneeID() {
        return assigneeID;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public String getAssigneeFirstname() {
        return assigneeFirstname;
    }

    public String getAssigneeSurname() {
        return assigneeSurname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskID);
        dest.writeString(this.taskTitle);
        dest.writeString(this.assigneeID);
        dest.writeString(this.assigneeEmail);
        dest.writeString(this.assigneeFirstname);
        dest.writeString(this.assigneeSurname);
    }
}
