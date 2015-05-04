package com.example.EECS_581.ecc_android_app;

import android.widget.Filter;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by eric on 4/27/15.
 */
public class NotesFilter extends Filter {
    private NotesArrayAdapter notesAdapter;
    private List<Note> allNotes;
    private List<Note> filteredNotes;

    public NotesFilter(NotesArrayAdapter nadapter) {
        this.notesAdapter = nadapter;
        this.allNotes = notesAdapter.getValues();
        this.filteredNotes = notesAdapter.getFilteredValues();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults result = new FilterResults();
        constraint = constraint.toString().toLowerCase();
        final ArrayList<Note> original = new ArrayList<Note>(allNotes);
        final ArrayList<Note> filtered = new ArrayList<Note>();

        if (constraint == null || constraint.length() == 0) {
            result.values = allNotes;
            result.count = allNotes.size();
        } else if (constraint.equals("interviews")){
            //TODO filters
            result.values = allNotes;
            result.count = allNotes.size();
        }

        return result;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredNotes = (List<Note>) results.values;
        notesAdapter.notifyDataSetChanged();
        notesAdapter.clear();

        for (int i = 0; i < filteredNotes.size(); i++) {
            notesAdapter.add(filteredNotes.get(i));
            notesAdapter.notifyDataSetInvalidated();
        }

    }
}
