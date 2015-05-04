package com.example.EECS_581.ecc_android_app;


import android.app.AlertDialog;
import android.text.method.KeyListener;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.AsyncTask;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.content.Intent;
import java.io.File;
import android.content.Context;
import android.widget.SearchView;


import org.json.JSONObject;
import org.json.JSONArray;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.EECS_581.ecc_android_app.R.layout.activity_notes;


/**
 * Created by eric on 4/27/15.
 */
public class Notes extends Fragment {

    String noteArchiveFilename;
    //The actual Java File object which points to the plaintext internal storage file to which notes
    //are saved.
    File noteArchiveDataFile;

    public static ArrayList<Note> notesList = new ArrayList<Note>();

    private ViewFlipper notesViewFlipper;

    //The EditText fields for entering a Note's Title and Body fields.
    EditText noteTitle;
    EditText noteBody;
    EditText noteCompanyName;
    EditText noteType;

    //Buttons for user control of the activity. Self explanatory.
    private Button newNoteButton;
    private Button saveNoteButton;//EditNoteMode element
    private Button discardDraftButton;//EditNoteMode element
    private Button editNoteButton;//ViewNoteMode element
    private Button deleteNoteButton;//ViewNoteMode element

    //An alert dialogue builder specifically for warning the user about empty notes.
    AlertDialog emptyNoteAlert;

    private KeyListener mainKeyListener;//The main key listener, used to re-enable edittext fields.

    View view;
    public static NotesArrayAdapter notesAdapter;

    ListView notesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        view = inflater.inflate(activity_notes, container, false);
        notesView = (ListView) view.findViewById(R.id.notesListView);


        new NotesAsyncTask().execute();


        return view;
    }

    private void initializeNotesClass(){
        //Next initialize the note archive filename, again referencing strings.xml.
        noteArchiveFilename = getResources().getString(R.string.noteArchiveFilename);

        //Then try to open the note archive data file, creating it if it doesn't yet exist.
        noteArchiveDataFile = new File(
                getActivity().getApplicationContext().getFilesDir(),
                noteArchiveFilename);


        //Then get each of the relevant XML elements,
        notesViewFlipper = (ViewFlipper) view.findViewById(R.id.notesViewFlipper);

        noteTitle = (EditText) view.findViewById(R.id.noteTitleEditView);
        noteBody  = (EditText) view.findViewById(R.id.noteBodyEditView);

        newNoteButton = (Button) view.findViewById(R.id.make_new_note_button);
        saveNoteButton = (Button) view.findViewById(R.id.saveNoteButton);//EditNoteMode
        discardDraftButton = (Button) view.findViewById(R.id.discardDraftButton);
        editNoteButton = (Button) view.findViewById(R.id.editNoteButton);//ViewNoteMode
        deleteNoteButton = (Button) view.findViewById(R.id.deleteNoteButton);


        //And build the empty note alert dialog. Use just getActivity() here; otherwise it crashes
        //on displaying the constructed alert dialog.
        AlertDialog.Builder emptyNoteADB = new AlertDialog.Builder(
                getActivity());
        emptyNoteADB.setTitle(R.string.emptyNoteDialogTitle);
        emptyNoteADB.setMessage(R.string.emptyNoteDialogMessage);
        emptyNoteADB.setNegativeButton(R.string.emptyNoteDialogOK,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        emptyNoteAlert = emptyNoteADB.create();

        notesView = (ListView)view.findViewById(R.id.notesListView);

        mainKeyListener = noteTitle.getKeyListener();

        //Setting the action of the Make New Note Button.
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchNoteViewMode("Edit");
                //Set the View to the viewNote portion.
                notesViewFlipper.setDisplayedChild(
                        notesViewFlipper.indexOfChild(
                                view.findViewById(R.id.viewNoteScreen)));

            /*
            //3. Exit back to this activity (also in CN), showing the new note in the list!

            //TODO: this will probably demand dynamic creation of buttons, or other XML elements
            //TODO: which will be then accessed. Figure it out! (see NoteArrayAdapter.java)
            */
            }
        });

        //Setting the action of the Save Note Button.
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String titleToSave = noteTitle.getText().toString();
                String bodyToSave = noteBody.getText().toString();

                //If either field is empty, notify the user and do not save the note.
                if(titleToSave.trim().isEmpty() || bodyToSave.trim().isEmpty()){
                    emptyNoteAlert.show();
                }
                else {
                    //Otherwise, save the note, and then...
                    saveNoteRecord(titleToSave,bodyToSave);

                    //Flush the EditText fields. Next...
                    noteTitle.setText("");
                    noteBody.setText("");


                    switchNoteViewMode("View");
                    //Set the View to the notesList portion.
                    notesViewFlipper.setDisplayedChild(
                            notesViewFlipper.indexOfChild(
                                    view.findViewById(R.id.notesListScreen)));
                }
            }
        });

        //Setting the action of the Discard Draft Button.
        discardDraftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Flush the noteTitle and noteBody fields, and then...
                noteTitle.setText("");
                noteBody.setText("");

                switchNoteViewMode("View");
                //Switch back to the notesList portion without saving.
                notesViewFlipper.setDisplayedChild(
                        notesViewFlipper.indexOfChild(
                                view.findViewById(R.id.notesListScreen)));
            }
        });

        //Setting the action of the Edit Note Button.
        editNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //First save the original note contents, in case edits are discarded.
                String originalTitle = noteTitle.getText().toString();
                String originalBody = noteBody.getText().toString();

                //Then switch to "Edit" mode by dis/enabling the proper fields and buttons, making
                // them visible and capable of interaction.
                switchNoteViewMode("Edit");
            }
        });

        //Setting the action of the Delete Note Button.
        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String titleToDelete = noteTitle.getText().toString();
                String bodyToDelete = noteBody.getText().toString();

                //Flush the noteTitle and noteBody fields, and then...
                noteTitle.setText("");
                noteBody.setText("");

                deleteNote(titleToDelete, bodyToDelete);

                switchNoteViewMode("View");

                //Switch back to the notesList portion without saving.
                notesViewFlipper.setDisplayedChild(
                        notesViewFlipper.indexOfChild(
                                view.findViewById(R.id.notesListScreen)));
            }
        });
        switchNoteViewMode("View");//View mode active by default.
    }

    void saveNoteRecord(String title, String body){
        System.out.println("Want to save..." + title + "  body: " + body);
        try {

            File file = getActivity().getBaseContext().getFileStreamPath("notes");

            JSONArray arr = getNotes();


            FileOutputStream noteArchiveOutput = new FileOutputStream(noteArchiveDataFile);
            JSONObject newNote = new JSONObject();
            newNote.put("title", title);
            newNote.put("body", body);
            newNote.put("company_name", "company");
            newNote.put("type","type");
            arr.put(newNote);
            JSONObject container = new JSONObject();
            container.put("notesArray", arr);
            noteArchiveOutput.write(container.toString().getBytes());

            Note n = new Note(title,body,"company","type");
            notesList.add(n);
            notesAdapter.notifyDataSetChanged();

            noteArchiveOutput.close();

        } catch(Exception e){
            if(e instanceof FileNotFoundException){
                Log.e("Notes.saveNoteRecord()","FileNotFoundException: Note archive not found!");
                //TODO special handling for each exception type!
            }
            else if(e instanceof IOException){
                Log.e("Notes.saveNoteRecord()","IOException: Unable to write to Note archive!");
                //TODO special handling for each exception type!
            }
        }
    }

    void deleteNote(String title, String body) {
        try {

            File file = getActivity().getBaseContext().getFileStreamPath("notes");
            JSONArray arr = getNotes();
            JSONArray updated = new JSONArray();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject currNote = (JSONObject) arr.get(i);
                String currTitle = ((String) currNote.get("title"));
                String currBody = ((String) currNote.get("body"));

                if (!currTitle.equals(title) && !currBody.equals(body)) {
                    updated.put(currNote);
                }
            }

            JSONObject container = new JSONObject();
            container.put("notesArray", updated);
            FileOutputStream noteArchiveOutput = new FileOutputStream(noteArchiveDataFile);
            noteArchiveOutput.write(container.toString().getBytes());
            noteArchiveOutput.close();


            for (int j = 0; j < notesList.size(); j++) {
                if (notesList.get(j).getTitle().equals(title) && notesList.get(j).getBody().equals(body)) {
                    notesList.remove(j);
                    notesAdapter.notifyDataSetChanged();
                    break;
                }
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    JSONArray getNotes() {
        JSONArray arr = new JSONArray();
        try {
            File file = getActivity().getBaseContext().getFileStreamPath("notes");

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bufferedStream = new BufferedInputStream(fis);
            InputStreamReader reader = new InputStreamReader(bufferedStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }

            String result = new String(builder.toString());
            JSONObject notesJson = new JSONObject(result);
            arr = notesJson.getJSONArray("notesArray");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arr;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
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

    /*
     * A function to easily switch between different note viewing "modes", AKA function and feature
     * sets. Current included modes are listed and paraphrased below:
     * 0. View Note: this mode handles simple viewing of a note, and has buttons to allow editing or
     *    deletion. This mode is also the default of the modes.
     * 1. Edit Note: this mode is reached by clicking "Edit Note" from the "View Note" mode, and
     *    handles editing of existing notes, as well as the creation of brand new ones. The
     *    "Save Note" and "Discard Draft" buttons are enabled.
     *
     * If no proper mode is given via the "mode" parameter, it will default to the "View" mode. For
     * clarity, all calls to this function intending to invoke the "View" mode should still include
     * that as a parameter.
     */

    private void switchNoteViewMode(String mode){
        switch(mode){
            case "Edit":
                //TODO refine interface so buttons irrelevant to current mode aren't even visible
                /*
                noteTitle.setEnabled(true);
                noteTitle.setFocusable(true);
                noteBody.setEnabled(true);
                noteBody.setFocusable(true);
                */
                noteTitle.setKeyListener(mainKeyListener);
                noteBody.setKeyListener(mainKeyListener);

                //saveNoteButton.setVisibility(View.VISIBLE);
                saveNoteButton.setEnabled(true);

                //discardDraftButton.setVisibility(View.VISIBLE);
                discardDraftButton.setEnabled(true);

                //editNoteButton.setVisibility(View.GONE);
                editNoteButton.setEnabled(false);

                //deleteNoteButton.setVisibility(View.GONE);
                deleteNoteButton.setEnabled(false);

                break;
            default://AKA the "View" case.
                //TODO refine interface so buttons irrelevant to current mode aren't even visible
                /*
                noteTitle.setEnabled(false);
                noteTitle.setFocusable(false);
                noteBody.setEnabled(false);
                noteBody.setFocusable(false);
                */
                noteTitle.setKeyListener(null);
                noteBody.setKeyListener(null);

                //saveNoteButton.setVisibility(View.GONE);
                saveNoteButton.setEnabled(false);
                //discardDraftButton.setVisibility(View.GONE);
                discardDraftButton.setEnabled(false);
                //editNoteButton.setVisibility(View.VISIBLE);
                editNoteButton.setEnabled(true);
                //deleteNoteButton.setVisibility(View.VISIBLE);
                deleteNoteButton.setEnabled(true);
        }


    }




    private class NotesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            BufferedInputStream bufferedStream;
            try {
                File file = getActivity().getBaseContext().getFileStreamPath("notes");

                FileInputStream fis = new FileInputStream(file);
                bufferedStream = new BufferedInputStream(fis);
                InputStreamReader reader = new InputStreamReader(bufferedStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }

                String result = new String(builder.toString());

                if (notesList.size() == 0) {
                    JSONObject notesJson = new JSONObject(result);
                    JSONArray arr = notesJson.getJSONArray("notesArray");

                    for (int i = 1; i < arr.length(); i++) {
                        JSONObject curNote = (JSONObject) arr.get(i);

                        String noteTitle = ((String) curNote.get("title"));
                        String noteBody = ((String) curNote.get("body"));
                        String company_name = ((String) curNote.get("company_name"));
                        String type = ((String) curNote.get("type"));

                        Note n = new Note(noteTitle, noteBody, company_name, type);
                        notesList.add(n);
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }

            return "";
        }
        protected void onPostExecute(String result) {
            initializeNotesClass();
            ListView listView = (ListView) view.findViewById(R.id.notesListView);

            notesAdapter = new NotesArrayAdapter(getActivity(), notesList);

            listView.setAdapter(notesAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Note note = notesList.get(position);//First, get the whole note.

                    noteTitle.setText(note.getTitle());
                    noteBody.setText(note.getBody());

                    //Then switch to seeing the Note!
                    notesViewFlipper.setDisplayedChild(
                            notesViewFlipper.indexOfChild(
                                    notesView.findViewById(R.id.viewNoteScreen)));

                }
            });
        }
    }

}
