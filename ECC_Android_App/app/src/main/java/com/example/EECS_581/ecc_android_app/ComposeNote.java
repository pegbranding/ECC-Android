package com.example.EECS_581.ecc_android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ncolom on 1/26/15.
 *
 * TODO: forbid titles from including | characters!
 *
 * Description: A "sub-activity" to the Notes activity which is actually responsible for composing
 * and saving the user's Notes. Internal storage is used to save data, thus allowing note titles and
 * bodies to be correlated to each other. See the string at the top of the class for the filename.
 * Format of the file is as thus (sans ellipses, with parentheticals only as descriptions):
 *
 * Concept: |record|(actualTitleText)|separator|(actualBodyText)|/record|...
 *
 * Literal: |r|Example title!|s|Meet at 8PM.|r|Title 2!|s|body2.
 *
 * Thus, a new title tag denotes the end of one Note record, and the beginning of the next (if
 * existent). Also, separators show separation between different data in a given Note record, thus
 * allowing future expansions on what data is saved. Data is appended to the file, so new notes are
 * added towards the bottom, and if any notes are deleted, all others are shifted "up" to fill the
 * gap.
 */
public class ComposeNote extends Activity{

    //The filename for the file that holds all saved note data. Do not change this without careful
    //consideration!
    final String noteArchiveFilename = getString(R.string.noteArchiveFilename);

    //The noteArchive full filename, including the files directory as a prefix. At this point, the
    //"Abstract File Path" is created, but the file is not actually made yet. Creation is handled
    //in onCreate.
    //File noteArchive = new File(getFilesDir() + noteArchiveFilename);

    EditText noteTitle;
    EditText noteBody;

    Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_note);

        saveNoteButton = (Button)findViewById(R.id.saveNoteButton);
        noteTitle = (EditText)findViewById(R.id.noteTitleEditView);
        noteBody  = (EditText)findViewById(R.id.noteBodyEditView);

        /*
        //If the noteArchive doesn't yet exist, create it! Otherwise leave it alone.
        if(!noteArchive.exists()){
            noteArchive.mkdir();
        }
        */

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //First, acquire the data to save in string form.
                final String titleToSave = noteTitle.getText().toString();
                final String bodyToSave = noteBody.getText().toString();

                //If either field is empty, notify the user and do not save the note.
                //TODO: also stop if either is only whitespace?
                if(titleToSave.isEmpty() || bodyToSave.isEmpty()){
                    //TODO build AlertDialog and use it here

                }
                else {
                    //Otherwise, call this helper function to complete all writing!
                    saveNoteRecord(titleToSave, bodyToSave);

                    //Then switch back to the Notes activity.
                    Intent notesAct=new Intent(v.getContext(),Notes.class);
                    startActivity(notesAct);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
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

    //A helper function to clean up the onCreate() code. This handles all writing of actual data to
    //the noteArchive, including organizing tags.
    void saveNoteRecord(String title, String body){
        //First, declare the tags that separate Note records,
        final String recordStart =  "|r|";
        final String separator   = "|s|";

        try{
            FileOutputStream noteArchiveOutput = openFileOutput(noteArchiveFilename,MODE_APPEND);

            //Then write the record beginning tag,
            noteArchiveOutput.write(recordStart.getBytes());
            //Title text,
            noteArchiveOutput.write(title.getBytes());
            //And separator tag.
            noteArchiveOutput.write(separator.getBytes());

            //Next up write the body, and you're done!
            noteArchiveOutput.write(body.getBytes());

            noteArchiveOutput.close();
        }
        catch(Exception e){
            if(e instanceof FileNotFoundException){
                //TODO special handling for each exception type!
            }
            else if(e instanceof IOException){
                //TODO special handling for each exception type!
            }
        }
    }
}