package com.example.affinityDemo;

import android.app.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class CentralActivity extends Activity  implements StartUpFragment.onNextButtonPressed , OverDueTasksFragment.onListSelected{

    private View actionBarView;
    private ActionBar actionBar;
    private Fragment startUpFragment;
    private Fragment overDueFragment;
    private Fragment taskDetailsFragment;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private ArrayList<Task> taskList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        actionBarView = getWindow().getDecorView(); // hide the action bar
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        actionBarView.setSystemUiVisibility(uiOptions);

        actionBar = getActionBar();
        actionBar.hide();


        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        startUpFragment = new StartUpFragment();
        transaction.replace(R.id.baseContainer, startUpFragment); // create the fragment
        transaction.commit();


    }

    public interface Item{
        public int getViewType();
        public View getView(LayoutInflater inflater, View convertView);
    }


    public void onButtonPress(String activationCode)
    {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        overDueFragment = new OverDueTasksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activationCode",activationCode); // bundle the activation code up
        overDueFragment.setArguments(bundle);
        transaction.replace(R.id.baseContainer,overDueFragment); // create the fragment
        transaction.commit();
    }


    @Override
    public void onListElementSelected(Task task) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        taskDetailsFragment = new com.example.affinityDemo.taskDetailsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelable("task", task);

        taskDetailsFragment.setArguments(bundle); // bundle the selected task
        transaction.replace(R.id.baseContainer,taskDetailsFragment); // create the fragment
        transaction.addToBackStack("List");
        transaction.commit();
    }
}
