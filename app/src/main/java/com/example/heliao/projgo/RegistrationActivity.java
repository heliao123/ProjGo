package com.example.heliao.projgo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        LoginFragment loginFragment = new LoginFragment();
        FragmentManager registrationManager = getFragmentManager();
        FragmentTransaction registrationTransaction = registrationManager.beginTransaction();
        registrationTransaction.add(R.id.login_main_frame,loginFragment);
        registrationTransaction.commit();
    }
}
