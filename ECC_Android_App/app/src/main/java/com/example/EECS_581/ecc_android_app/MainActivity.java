package com.example.EECS_581.ecc_android_app;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class MainActivity extends ActivityGroup {

    TabHost tabHost;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup(getLocalActivityManager());

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("List");
        tabSpec1.setIndicator("List");
        Intent listIntent = new Intent(this, CompanyList.class);
        tabSpec1.setContent(listIntent);
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Map");
        tabSpec2.setIndicator("Map");
        Intent mapIntent = new Intent(this, Map.class);
        tabSpec2.setContent(mapIntent);
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("Notes");
        tabSpec3.setIndicator("Notes");
        Intent notesIntent = new Intent(this, Notes.class);
        tabSpec3.setContent(notesIntent);
        tabHost.addTab(tabSpec3);

    }
}
