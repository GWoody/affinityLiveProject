package com.example.affinityDemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Gareth on 18/12/2014.
 * fragment that greets the user with sign on an authentication methods
 */
public class StartUpFragment extends Fragment {

    private Button  requestPermissionButton;
    private Button  loginButton;
    private EditText authCodeField;
    private ImageView imageView;

    private int authCount = 1;
    private TextWatcher textWatcher;
    private onNextButtonPressed callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.startup, container, false);
    }

    // attach override to the activity and init the callback
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try{
            callback = (onNextButtonPressed) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "implement on Next Button selected");
        }
    }

    public interface  onNextButtonPressed
    {
        public void onButtonPress(String activationCode);
    }

    @Override
    public void onStart() {
        super.onStart();

        requestPermissionButton = (Button) getView().findViewById(R.id.requestButton);
        loginButton = (Button) getView().findViewById(R.id.loginButton);
        authCodeField = (EditText) getView().findViewById(R.id.authCodeField);


        imageView = (ImageView) getView().findViewById(R.id.logo);

        Bitmap logo = retrieveAsset("affinitylive.png"); // assign the image to the view
        imageView.setImageBitmap(logo);


        loginButton.setVisibility(View.INVISIBLE); // prevent the user from logging in

        requestPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Credentials newUser = new Credentials();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newUser.getAuthorizationURL()));
                startActivity(browserIntent); // allow the user to launch the browser to login

            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                authCount = Integer.valueOf(s.length());

                if(authCount <= 10 && authCount >= 9)
                {
                    loginButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Valid code ", Toast.LENGTH_SHORT).show(); // check the length of the code


                }
                if(authCount <= 0 || authCount >= 11)
                {
                    Toast.makeText(getActivity(),"Invalid Authentication code",Toast.LENGTH_SHORT).show(); // prevent access otherwise
                    loginButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        authCodeField.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authCode = authCodeField.getText().toString();
                callback.onButtonPress(authCode);
            }
        });


    }

    public Bitmap retrieveAsset(String assetName) // class to retrieve the assets from the folder assets
    {
        AssetManager assetManager = getActivity().getAssets();
        InputStream inputStream = null;
        try{
            inputStream = assetManager.open(assetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}