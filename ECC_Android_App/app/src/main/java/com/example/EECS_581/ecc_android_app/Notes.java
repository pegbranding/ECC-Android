package com.example.EECS_581.ecc_android_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.EECS_581.ecc_android_app.R.layout.activity_company_list;

public class Notes extends Fragment {

    //=-=-=-=-=- Class Variable Definitions =-=-=-=-=-
    //Each of these is initialized via the initializeNotesClass() method below. All lone Strings
    //below reference strings.xml for their data. Do not change any of these without careful
    //thought!
    String recordStart;
    String separator;
    String noteArchiveFilename;

    //The actual Java File object which points to the plaintext internal storage file to which notes
    //are saved.
    File noteArchiveDataFile;
    boolean firstWrite = true;//Whether this run of the program has conducted the first write of the
    //note archive. Controls whether to overwrite or append, to avoid polluting the archive.

    private ViewFlipper notesViewFlipper;

    //The EditText fields for entering a Note's Title and Body fields.
    EditText noteTitle;
    EditText noteBody;

    //Buttons for user control of the activity. Self explanatory.
    private Button newNoteButton;
    private Button saveNoteButton;//EditNoteMode element
    private Button discardDraftButton;//EditNoteMode element
    private Button editNoteButton;//ViewNoteMode element
    private Button deleteNoteButton;//ViewNoteMode element

    //An alert dialogue builder specifically for warning the user about empty notes.
    AlertDialog emptyNoteAlert;

    //The ListView which contains the list of written notes.
    ListView notesListView;//TODO finish! (complete noteArrayAdapter to make each row look nice)
    ArrayList<String> notesList;//The ArrayList containing the actual notes themselves.

    View notesView;//The View for this portion of the activity, allowing Fragment compatibility.
    private KeyListener mainKeyListener;//The main key listener, used to re-enable edittext fields.


    //=-=-=-=-=- /Class Variable Definitions =-=-=-=-=-
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesView = inflater.inflate(R.layout.activity_notes,container,false);
        initializeNotesClass();
        return notesView;
    }

    /* A helper function to the class to initialize all utilized class variables. Intended for call
     * from onCreate() ONLY!
     */
    private void initializeNotesClass(){
        //First, initialize the note start and note separator markers (via reference to strings.xml)
        recordStart = getResources().getString(R.string.noteRecordSeparator);
        separator = getResources().getString(R.string.noteSeparatorMarker);

        //Next initialize the note archive filename, again referencing strings.xml.
        noteArchiveFilename = getResources().getString(R.string.noteArchiveFilename);

        //Then try to open the note archive data file, creating it if it doesn't yet exist.
        noteArchiveDataFile = new File(
                getActivity().getApplicationContext().getFilesDir(),
                noteArchiveFilename);

        try {
            if (!noteArchiveDataFile.exists()) {
                FileOutputStream temp = getActivity().getApplicationContext().openFileOutput(
                        noteArchiveFilename, Context.MODE_APPEND);
                temp.write(1);//TODO CONTINUE FROM HERE NEXT TIME! write seems to force file
                //creation.
                if (!noteArchiveDataFile.exists()) {
                    Log.e("Notes.initializeNotesClass()","Unable to create the note archive!");
                }
            }
            else{
                firstWrite = false;
            }
        }
        catch (IOException e){
            Log.e("Notes.initializeNotesClass()","IOException: Note archive not created!");
        }

        Log.e("Notes.initializeNotesClass()",noteArchiveDataFile.toString());

        //Then get each of the relevant XML elements,
        notesViewFlipper = (ViewFlipper) notesView.findViewById(R.id.notesViewFlipper);

        noteTitle = (EditText) notesView.findViewById(R.id.noteTitleEditView);
        noteBody  = (EditText) notesView.findViewById(R.id.noteBodyEditView);

        newNoteButton = (Button) notesView.findViewById(R.id.make_new_note_button);
        saveNoteButton = (Button) notesView.findViewById(R.id.saveNoteButton);//EditNoteMode
        discardDraftButton = (Button) notesView.findViewById(R.id.discardDraftButton);
        editNoteButton = (Button) notesView.findViewById(R.id.editNoteButton);//ViewNoteMode
        deleteNoteButton = (Button) notesView.findViewById(R.id.deleteNoteButton);


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

        notesListView = (ListView)notesView.findViewById(R.id.notesListView);

        mainKeyListener = noteTitle.getKeyListener();

        //Setting the action of the Make New Note Button.
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchNoteViewMode("Edit");
                //Set the View to the viewNote portion.
                notesViewFlipper.setDisplayedChild(
                        notesViewFlipper.indexOfChild(
                                notesView.findViewById(R.id.viewNoteScreen)));

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

                final String titleToSave = noteTitle.getText().toString();
                final String bodyToSave = noteBody.getText().toString();

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

                    //Refresh the notes list now, so when the next code line occurs, the list is
                    //already populated.
                    refreshNotesList();

                    switchNoteViewMode("View");
                    //Set the View to the notesList portion.
                    notesViewFlipper.setDisplayedChild(
                            notesViewFlipper.indexOfChild(
                                    notesView.findViewById(R.id.notesListScreen)));
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
                                notesView.findViewById(R.id.notesListScreen)));
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
                //Flush the noteTitle and noteBody fields, and then...
                noteTitle.setText("");
                noteBody.setText("");
                //TODO write a (real) deletion function! Not a complete purge!(first see TODO 0)
                purgeNoteArchive();

                switchNoteViewMode("View");

                //Switch back to the notesList portion without saving.
                notesViewFlipper.setDisplayedChild(
                        notesViewFlipper.indexOfChild(
                                notesView.findViewById(R.id.notesListScreen)));
            }
        });
        switchNoteViewMode("View");//View mode active by default.
        //Also complete the initial note list refresh.
        refreshNotesList();
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
     * A function to refresh the displayed list of Notes.
     */
    void refreshNotesList() {
        notesList = getNoteList();//First, acquire an updated String ArrayList of notes.

        if(notesList != null){//If notes exist, proceed to populate the list.
            ArrayAdapter<String> notesArrayAdapter = new ArrayAdapter<>(
                    getActivity().getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    notesList);
            notesListView.setAdapter(notesArrayAdapter);

            notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String note = notesList.get(position);//First, get the whole note.

                    //Then split it into its component Title and Body.
                    String thisNoteTitle = note.substring(3,note.lastIndexOf(separator));
                    String thisNoteBody = note.substring(note.indexOf(separator)+3,note.length());

                    //Then set the text of the note.
                    noteTitle.setText(thisNoteTitle);
                    noteBody.setText(thisNoteBody);

                    //Then switch to seeing the Note!
                    notesViewFlipper.setDisplayedChild(
                            notesViewFlipper.indexOfChild(
                                    notesView.findViewById(R.id.viewNoteScreen)));

                    //TODO ALSO! Make sure that saveNote doesn't produce duplicates (as per above)!
                }
            });
            //TODO FINISH!
            //TODO finish this method! Look to the following resources:
            //http://developer.android.com/guide/topics/ui/layout/listview.html
            //http://developer.android.com/guide/topics/ui/declaring-layout.html#AdapterViews
            //http://www.vogella.com/tutorials/AndroidListView/article.html
            //http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html

        }
        else{
            //If the noteList is null, do not populate the list at all.
        }
    }

    /*
     * A helper function to write notes to the note archive, including the Title and Body.
     * NOTE RECORD FORMAT: 2015-02-23
     * "|r|"<title>"|s|"<noteBody> ...
     * Text in quotes are literal, text in <>'s indicate what type of text should go there.
     * WARNING! Usage of | symbols within notes is disallowed, and each will be replaced with a
     * forward slash (/).
     */
    void saveNoteRecord(String title, String body){
        try{
            FileOutputStream noteArchiveOutput = new FileOutputStream(
                    noteArchiveDataFile, !firstWrite);

            //First cleanse note data of | symbols, which could cause errors in note handling else
            //where.
            String cleanedTitle = title.replace("|","/");
            String cleanedBody  = body.replace("|","/");

            //Then write the record beginning tag,
            noteArchiveOutput.write(recordStart.getBytes());
            //Title text,
            noteArchiveOutput.write(cleanedTitle.getBytes());
            //And separator tag.
            noteArchiveOutput.write(separator.getBytes());

            //Next up write the body, and you're done!
            noteArchiveOutput.write(cleanedBody.getBytes());

            if(firstWrite){
                firstWrite = false;
            }

            //TODO REMOVE DEBUGGING OUTPUT
            //Log.e("Notes.saveNoteRecord()","(title,body): " + cleanedTitle + ","+cleanedBody);
            noteArchiveOutput.close();
        }
        catch(Exception e){
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

    /*
     * A function to obtain and return the full list of notes from internal plaintext storage, if
     * present. If no notes currently exist, the returned ArrayList<String> will be null. Otherwise,
     * each element of the ArrayList will hold one note, including all formatting markers (|r|, |s|,
     * etc. as of 2015-03-02).
     */
    ArrayList<String> getNoteList(){
        ArrayList<String> listOfNotes = null;
        try{
            listOfNotes = new ArrayList<>();//<> only since String was specified in initialization.
            FileInputStream noteArchiveInput = new FileInputStream(noteArchiveDataFile);

            //TODO remove debugging code here
            //Log.e("Notes.getNoteList()","FileInputStream: "+noteArchiveInput.toString());
            //Log.e("Notes.getNoteList()","File: "+noteArchiveDataFile.toString());

            //First obtain the whole note archive.
            //TODO replace this code with more efficient reading code?
            StringBuilder builder = new StringBuilder();
            int readData;
            while((readData = noteArchiveInput.read()) != -1){
                builder.append((char)readData);
            }
            noteArchiveInput.close();

            //Then produce the array of its components,
            String tempArchiveCopy = builder.toString();

            //Log.e("tAC:",tempArchiveCopy);

            //And produce a list of strings, each element corresponding to one note.
            while( tempArchiveCopy.contains(recordStart) ) {
                int firstMarkIndex,secondMarkIndex;//The indices of the first and second found |r|.
                firstMarkIndex = tempArchiveCopy.indexOf(recordStart);
                secondMarkIndex = tempArchiveCopy.indexOf(recordStart,3);

                if(firstMarkIndex < 0){//If no records exist,
                    break;//break out to avoid errors!
                }
                //If no second record exists, the archive end is the (one and only) note's end.
                if(secondMarkIndex < 0){
                    secondMarkIndex = tempArchiveCopy.length();
                }

                String nextNote = tempArchiveCopy.substring(firstMarkIndex,secondMarkIndex);
                //adds <recordStart><title><separator><body> to the listOfNotes.
                listOfNotes.add(nextNote);
                //Don't forget to truncate the just-found note from the tempArchiveCopy!
                tempArchiveCopy = String.copyValueOf(
                        tempArchiveCopy.toCharArray(),
                        nextNote.length(),
                        tempArchiveCopy.length() - nextNote.length());
            }
        }
        catch(Exception e){
            if(e instanceof FileNotFoundException){
                Log.e("Notes.getNoteList()","FileNotFoundException: Note archive not found!");
                //TODO special handling for each exception type!
            }
            else if(e instanceof IOException){
                Log.e("Notes.getNoteList()","IOException: Unable to write to Note archive!");
                //TODO special handling for each exception type!
            }
        }
        return listOfNotes;
    }

    /*
     * A function to completely purge the note archive, deleting all entries! Do not use without
     * caution and careful consideration beforehand!
     */
    void purgeNoteArchive(){
        //TODO FIX! Currently doesn't /ever/ successfully delete the note archive!
        //Might this be due to it not using a full / absolute filepath? Investigate!
        try{
            noteArchiveDataFile.delete();
        }
        catch(Exception e){
            if(e instanceof SecurityException){
                Log.e("Notes.purgeNoteArchive()",
                        "SecurityException: Note archive deletion denied!");
                //TODO special handling for each exception type!
            }
        }
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
}
