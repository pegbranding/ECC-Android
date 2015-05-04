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
        String constraintString = constraint.toString();

        if (constraint == null || constraint.length() == 0) {
            result.values = allNotes;
            result.count = allNotes.size();
        } else {
            for (int i = 0; i < original.size(); i++) {
                final Note n = original.get(i);
                System.out.println("Type: " + n.getType());
                if(n.getType().equals("Interview") && constraintString.contains("interviews")) {
                    filtered.add(n);
                }
                if(n.getType().equals("Info Session") && constraintString.contains("info_sessions")) {
                    filtered.add(n);
                }
                if(n.getType().equals("Deadline") && constraintString.contains("deadlines")) {
                    filtered.add(n);
                }

            }
            result.values = filtered;
            result.count = filtered.size();
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
