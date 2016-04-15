package com.example.heliao.projgo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heliao.projgo.projgoServerData.*;

import android.content.SharedPreferences.Editor;
import android.widget.Toast;

/**
 * Created by HeLiao on 4/1/2016.
 */
public class LoginFragment extends Fragment {

    public EditText mUsername;
    String username;
    String password;
    public EditText mPassword;
    public TextView mUserRegistration;
    public Button mDone;
    SharedPreferences sharedPreferences;
    Editor editor;
    ServerDataManager dataManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = (View) inflater.inflate(R.layout.fragment_log_in, container, false);
        mUsername = (EditText) rootview.findViewById(R.id.userid_editTextt);
        mPassword = (EditText) rootview.findViewById(R.id.password_editText);
        mDone = (Button) rootview.findViewById(R.id.done_Button);
        mUserRegistration = (TextView) rootview.findViewById(R.id.user_registration_button);

        //create sharedpreferences

            mUserRegistration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserRegistrationFragment userRegistrationFragment = new UserRegistrationFragment();
                    FragmentManager loginmanager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = loginmanager.beginTransaction();
                    fragmentTransaction.replace(R.id.login_main_frame, userRegistrationFragment);
                    fragmentTransaction.commit();
                }
            });

            mDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username = mUsername.getText().toString();
                    password = mPassword.getText().toString();
                    if( dataManager.userList.containsKey(username)&&dataManager.userList.get(username).password==password){
                    // put username and password in sharedpreferences
                        editor.putString("nameKey", username);
                        editor.putString("passwordKey", password);
                        editor.commit();
                    //create intent and bundle, then sent it to **MainActivity**
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);}else
                        Toast.makeText(getActivity().getApplicationContext(),"User name or passwork is incorrect~!",Toast.LENGTH_LONG).show();
                }
            });
        //} catch (NullPointerException e) {
       //     System.out.print(e.toString());
        //}
        return rootview;
    }
}
