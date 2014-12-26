package com.example.affinityDemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Gareth on 18/12/2014.
 */
public class OverDueTasksFragment extends Fragment {

    private static String activationCode = ""; // collecting the code the user entered;
    private static outdatedTaskAdapter adapter; // the adapter for the list
    private static ArrayList<CentralActivity.Item> ItemList = new ArrayList<CentralActivity.Item>(); // the item list ( keep persistant to prevent reload)
    private static HashSet<String> headerList; // list of header elements for the listview
    private static ArrayList<Task> tempTaskList; // list of tasks to check grouping with headers
    private onListSelected callback; // callback to activity
    private ActionBar actionBar; // the actionbar
    private int currentPage = 0; // the currentpage we are seeking

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.overduetask, container, false); // inflate the layout

        getActivity().setTitle("Overdue tasks");

        headerList = new HashSet<String>(); // init the lists
        tempTaskList = new ArrayList<Task>();

        actionBar = getActivity().getActionBar(); // show the activity bar
        actionBar.show();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff538aff"))); // set background color

        setHasOptionsMenu(true); // allow for action bar items

        Bundle bundle = this.getArguments();
        activationCode = bundle.getString("activationCode"); // receive the activationCode as an argument

        adapter = new outdatedTaskAdapter(getActivity(),ItemList); // init the adapter
        final ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter); // set it to the listview

        if(ItemList.size() == 0) // check if we need to reload
        {

            RecieveToken token = new RecieveToken(); // get our access Token
            token.execute();

            RecieveTasks tasks = new RecieveTasks(); // recieve the tasks
            tasks.execute(Integer.toString(currentPage)); // give it the current page
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CentralActivity.Item item = ItemList.get(position);
                int type  = item.getViewType(); // check the type of our listview element

                if(type == 0)
                {
                    ListItem newItem = (ListItem) item;
                    callback.onListElementSelected(newItem.getListTask()); // do a callback to the activity and launch the details fragment
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == SCROLL_STATE_IDLE)
            {
                if(listView.getLastVisiblePosition() >= listView.getCount() - 1 ) // check if we hit the end of the current set ( 10)
                {
                   currentPage++;
                    headerList.clear();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged(); // update the listview
                        }
                    });
                    RecieveTasks tasks = new RecieveTasks();
                    tasks.execute(Integer.toString(currentPage)); // repopulate the new page
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Loaded page: " + currentPage, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged(); // update the listview
                        }
                    });

                }
            }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { // needs to remain
            }
        });


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.refreshmenu,menu); // inflate the menu

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // allows for the updated status of tasks once they are move to complete

        ItemList.clear(); // clear the list
        headerList.clear(); // clear the header
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged(); // update listview
            }
        });
        RecieveTasks tasks = new RecieveTasks(); // reexecute and start from page 1
        tasks.execute(Integer.toString(0));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged(); // can't update this by itself (needs runnable around it )
            }
        });


        return true;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try{
            callback = (onListSelected) activity; // attach callback
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "implement on list selected");
        }
    }

    public interface  onListSelected
    {
        public void onListElementSelected(Task task); // pass the journey we want to deal with in the slides
    }

    private static void disableSSLCertificateChecking() { // EXTREMELY DANGEROUS AND IS ONLY USED TO IGNORE THE SSL ERRORS ****DO NOT USE *****
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) { // verify every session regardless
                    return true;
                }
            });
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom()); // init a certificate that allows all connections through

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private class RecieveToken extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

        Credentials newUser = new Credentials();
        disableSSLCertificateChecking(); // disable SSL error detection
            try {
                URL url = new URL(newUser.ReceiveCredientials(activationCode));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // formulate and execute url
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = br.readLine()) != null)
                {
                    response.append(line);
                }

                CredientialReader(response.toString()); // read the credentials

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

    public void CredientialReader(String response) throws JSONException {
        String accessToken = "";
        String refreshToken = "";
        JSONObject reader = new JSONObject(response);
        accessToken = reader.getString("access_token");
        refreshToken = reader.getString("refresh_token");

        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE); // open shared preferences to store the tokens
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token",accessToken);
        editor.putString("refresh_token",refreshToken);
        editor.commit();

    }

    public String getToken() // get a token from shared preferences
    {
        String accessToken = "";
        SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        accessToken = preferences.getString("access_token","");


        return  accessToken;

    }

    private class RecieveTasks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            TaskQueryStructure taskQueryStructure = new TaskQueryStructure();
            disableSSLCertificateChecking(); // disable SSL checking
            try {
                URL url = new URL(taskQueryStructure.generateTaskURL(getToken(),Integer.parseInt(params[0])));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = br.readLine()) != null)
                {
                    response.append(line);
                }

                TaskReader(response.toString()); // retrieve the tasks and read them into the list

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void TaskReader( String response)
    {
        ArrayList<Task> tempTaskList = new ArrayList<Task>();
        try {
            JSONObject reader = new JSONObject(response);
            JSONArray tasks = reader.getJSONArray("response");
            for(int i = 0; i < tasks.length(); i++) {

            Task newTask = new Task();
            JSONObject aTask = tasks.getJSONObject(i);
            newTask.setTaskTitle(aTask.getString("title"));
            newTask.setTaskID(aTask.getString("id"));

            if(aTask.isNull("assignee")) // if no assignee is present null out the fields
            {
                newTask.setAssigneeID("N/A");
                newTask.setAssigneeEmail("N/A");
                newTask.setAssigneeFirstname("N/A");
                newTask.setAssigneeSurname("N/A");
            }
            else
            {
                JSONObject assignee = aTask.getJSONObject("assignee");
                newTask.setAssigneeID(assignee.getString("id"));
                newTask.setAssigneeEmail(assignee.getString("email"));
                newTask.setAssigneeFirstname(assignee.getString("firstname"));
                newTask.setAssigneeSurname(assignee.getString("surname"));
            }

            headerList.add(newTask.getAssigneeEmail()); // add the email to the header list

            tempTaskList.add(newTask); // add the task to the list to check it later
            }

            for(String item : headerList)
            {

                ItemList.add(new HeaderItem(item)); // add the header into the item list

                for(int j =0; j < tempTaskList.size(); j++)
                {
                    Task tempTask = tempTaskList.get(j);
                    if(item.equals(tempTask.getAssigneeEmail())) // get a task and if it matches it is added right after the list
                    {
                        ItemList.add(new ListItem(tempTask));
                    }
                }

            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged(); // update the adapter
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}