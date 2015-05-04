package com.example.EECS_581.ecc_android_app;

import android.widget.Filter;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eric on 4/27/15.
 */
public class NotesArrayAdapter extends ArrayAdapter implements Filterable {

    public static ArrayList<Note> allNotesArray;
    public static ArrayList<Note> filteredNotesArray;
    private NotesFilter mFilter;

    private Activity context;
    private LayoutInflater inflater;

    public NotesArrayAdapter(Activity context, List<Note> list) {
        super(context, R.layout.notes_list_row_item, list);
        this.context = context;
        this.allNotesArray = new ArrayList<Note>();
        allNotesArray.addAll(list);
        this.filteredNotesArray = new ArrayList<Note>();
        filteredNotesArray.addAll(allNotesArray);
        this.mFilter = new NotesFilter(this);
        inflater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.notes_list_row_item, parent, false);
        }

        Note n = Notes.notesList.get(position);

        if (n != null) {
            TextView titlePreview = (TextView) rowView.findViewById(R.id.notesTitle);
            if (titlePreview != null) {
                titlePreview.setText(Notes.notesList.get(position).getTitle());
            }
            TextView typePreview = (TextView) rowView.findViewById(R.id.noteType);
            if (typePreview != null) {
                typePreview.setText(Notes.notesList.get(position).getType());
            }
        }

        return rowView;
    }

    public List<Note> getValues() { return allNotesArray; }

    public List<Note> getFilteredValues() { return filteredNotesArray; }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new NotesFilter(this);
        }
        return mFilter;
    }



}
