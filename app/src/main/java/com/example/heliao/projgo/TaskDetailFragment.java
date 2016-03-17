package com.example.heliao.projgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by HeLiao on 3/17/2016.
 */
public class TaskDetailFragment extends Fragment{
    TextView taskname;
    Button donebutton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.fragment_task_detail,container,false);
        taskname = (TextView)view.findViewById(R.id.taskname);
        donebutton = (Button)view.findViewById(R.id.buttonDone);
        final Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        return view;
    }



    public void updateinfo(String title){
        taskname.setText(title);
    };

}
