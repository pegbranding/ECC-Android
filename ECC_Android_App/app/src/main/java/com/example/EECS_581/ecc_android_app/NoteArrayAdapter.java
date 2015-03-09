package com.example.EECS_581.ecc_android_app;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by ncolom on 3/2/15.
 */
public class NoteArrayAdapter<String> extends ArrayAdapter<String> {

    //TODO complete this class to provide buttons in views that the user can click to view a given
    //TODO note!
    //TODO also, find a way to format the source note strings? (particularly to strip away |r|, |s|,
    //TODO etc.

    /*
    Resources for reference:

    http://developer.android.com/guide/topics/resources/more-resources.html
    http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
    ^^^^^^^^^^^^^^^^^^^^^^^^^^6
     */
    public NoteArrayAdapter(Context context, int xmlResourceID,List<String> source){
        super(context,xmlResourceID,source);

    }
}
