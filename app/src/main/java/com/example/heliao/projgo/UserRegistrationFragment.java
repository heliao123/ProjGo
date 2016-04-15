package com.example.heliao.projgo;

import android.app.Fragment;
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

import com.example.heliao.projgo.projgoServerData.ServerDataManager;
import com.example.heliao.projgo.projgoServerData.User;

/**
 * Created by HeLiao on 4/1/2016.
 */
public class UserRegistrationFragment extends Fragment {
    @Nullable
    EditText mUsername;
    EditText mUserpassword;
    Button mDone;
    String username;
    String password;
    User newuser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =(View)inflater.inflate(R.layout.fragment_user_register,container,false);
        mUsername =(EditText)rootview.findViewById(R.id.userid_editTextt_user_register);
        mUserpassword= (EditText)rootview.findViewById(R.id.password_editText_user_register);
        mDone = (Button)rootview.findViewById(R.id.done_Button_user_register);

        //create sharedpreferences
        sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new user as the user object
                username = mUsername.getText().toString();
                password = mUserpassword.getText().toString();
                newuser = new User(username,password);
                ServerDataManager dataManager = ServerDataManager.getInstance();
                dataManager.addUser(username,newuser);

                //save user info in the sharedpreferences
                editor = sharedPreferences.edit();
                editor.putString("nameKey",username);
                editor.putString("passwordKey",password);
                editor.commit();

                // pass user info to the Main Activity
                Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putString("password",password);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        return rootview;
    }
}
