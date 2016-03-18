package com.example.heliao.projgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class AddEventFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_add_layout,container,false);
       final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
        Button donebutton =(Button)view.findViewById(R.id.buttonDone);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        return view;
    }
}
