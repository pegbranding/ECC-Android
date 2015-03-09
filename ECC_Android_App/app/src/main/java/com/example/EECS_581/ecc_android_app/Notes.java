package com.example.EECS_581.ecc_android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import static com.example.EECS_581.ecc_android_app.R.layout.activity_company_list;


public class Notes extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notes, container, false);
        //Setting the action of the New Note Button.
        final Button newNoteButton = (Button) view.findViewById(R.id.make_new_note_button);
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //1. Switch to a ComposeNote activity.
                Intent homePageAct=new Intent(v.getContext(),MainActivity.class);
                startActivity(homePageAct);


                //2. Once in the CN activity, compose and save the note. (handled in ComposeNote).

                //3. Exit back to this Notes activity (this code), showing the new note in the list!
            }
        });
        return view;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
