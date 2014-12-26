package com.example.affinityDemo;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gareth on 22/12/2014.
 * fragment that handles display of detailed task information and allows the movement of the task to completed
 */
public class taskDetailsFragment extends Fragment {
    private TextView taskID;
    private TextView taskTitle;
    private TextView assigneeID;
    private TextView assigneeEmail;
    private TextView assigneeFirstname;
    private TextView assigneeSurname;
    private Task aTask;
    private Button doneButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.taskdetails, container, false);

        getActivity().setTitle("Task");

        taskTitle = (TextView) v.findViewById(R.id.taskName);
        taskID = (TextView) v.findViewById(R.id.taskID);
        assigneeID = (TextView) v.findViewById(R.id.assigneeID);
        assigneeEmail = (TextView) v.findViewById(R.id.assigneeEmail);
        assigneeFirstname = (TextView) v.findViewById(R.id.assigneeFirstName);
        assigneeSurname = (TextView) v.findViewById(R.id.assigneeSurname);
        doneButton = (Button) v.findViewById(R.id.doneButton);


        Bundle bundle = this.getArguments();
        aTask = (Task) bundle.getParcelable("task");
        taskTitle.setText( aTask.getTaskTitle() );
        taskID.setText(aTask.getTaskID());
        assigneeID.setText(aTask.getAssigneeID());
        assigneeEmail.setText(aTask.getAssigneeEmail());
        assigneeFirstname.setText(aTask.getAssigneeFirstname());
        assigneeSurname.setText(aTask.getAssigneeSurname());


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoveTaskToComplete taskToComplete = new MoveTaskToComplete();
                taskToComplete.execute();
            }
        });

        return v;
    }

    private class MoveTaskToComplete extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            TaskQueryStructure taskQueryStructure = new TaskQueryStructure();

            String accessToken = "";
            SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
            accessToken = preferences.getString("access_token", ""); // get the credientials and formulate the query

            try {
                URL url = new URL(taskQueryStructure.generateTaskCompletion(accessToken, aTask.getTaskID()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                TaskCompleteReader(response.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    public void TaskCompleteReader(String response) throws JSONException {

        JSONObject reader = new JSONObject(response);
        JSONObject metaInfo = reader.getJSONObject("meta");

        final String success = metaInfo.getString("status");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(success.equals("ok"))
                {
                    Toast.makeText(getActivity(), "Task moved to completed", Toast.LENGTH_SHORT).show(); // the task was moved to complete
                }
                else
                {
                    Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show(); // an error occured
                }
            }
        });



    }
}