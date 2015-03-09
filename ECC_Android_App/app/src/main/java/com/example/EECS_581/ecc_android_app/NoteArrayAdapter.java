package com.example.EECS_581.ecc_android_app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.lang.String;
/**
 * Created by ncolom on 3/2/15.
 */
public class NoteArrayAdapter extends ArrayAdapter<String> {

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
    public NoteArrayAdapter(Context context, int xmlResourceID, List<String> source){
        super(context,xmlResourceID,source);
    }

    //TODO figure out how to override this to properly provide a nice format for each row of the
    //ListView!
    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        /*
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_association, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.ItemView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyClass item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(String.format("%s %d", item.reason, item.long_val));
        }

        return view;

    }
    */
}
