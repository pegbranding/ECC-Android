package com.example.EECS_581.ecc_android_app;

import android.gesture.Gesture;
import android.support.v4.app.Fragment;
<<<<<<< Updated upstream
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
=======
import android.support.v4.view.ViewCompat;
>>>>>>> Stashed changes
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;



public class Map extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View V = inflater.inflate(R.layout.activity_map, container, false);
        //sets the original progress for the progressbar depending on which image it is looking at.
        final ViewFlipper ECCFlipper = (ViewFlipper)V.findViewById(R.id.eccflipper);
        ProgressBar eccprogress2 = (ProgressBar) V.findViewById(R.id.progressBar);
        int oldprogress = ((ECCFlipper.getDisplayedChild() * 100) / (ECCFlipper.getChildCount() - 1));
        eccprogress2.setProgress(oldprogress);

        Button nextButton = (Button)V.findViewById(R.id.nextbutton);
        Button previousButton = (Button)V.findViewById(R.id.previousbutton);
        ImageView image1 = (ImageView)V.findViewById(R.id.floor4image);
        ImageView image2 = (ImageView)V.findViewById(R.id.ballroomfloor5image);
        ImageView image3 = (ImageView)V.findViewById(R.id.big12floor5image);
        ImageView image4 = (ImageView)V.findViewById(R.id.kansasroomfloor6image);
        ImageView image5 = (ImageView)V.findViewById(R.id.malottroomfloor6image);



        View.OnClickListener newListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.nextbutton:

                        Toast.makeText(V.getContext(), "Next Map", Toast.LENGTH_SHORT).show();
                        //Finds the ViewFlipper and goes to the next image
                        ECCFlipper.setInAnimation(V.getContext(), android.R.anim.fade_in);
                        ECCFlipper.setOutAnimation(V.getContext(), android.R.anim.fade_out);
                        ECCFlipper.showNext();
                        //incrementing the progressbar
                        ProgressBar eccprogress = (ProgressBar) V.findViewById(R.id.progressBar);
                        int oldprogress = ((ECCFlipper.getDisplayedChild() * 100) / (ECCFlipper.getChildCount() - 1));
                        eccprogress.setProgress(oldprogress);

                    break;
                    case R.id.previousbutton:

                        Toast.makeText(V.getContext(), "Previous Map", Toast.LENGTH_SHORT).show();
                        //Finds the ViewFlipper and goes to the previous image
                        ECCFlipper.setInAnimation(V.getContext(), android.R.anim.fade_in);
                        ECCFlipper.setOutAnimation(V.getContext(), android.R.anim.fade_out);
                        ECCFlipper.showPrevious();
                        //decrementing the progressbar
                        ProgressBar eccprogress2 = (ProgressBar) V.findViewById(R.id.progressBar);
                        int oldprogress2 = ((ECCFlipper.getDisplayedChild() * 100) / (ECCFlipper.getChildCount() - 1));
                        eccprogress2.setProgress(oldprogress2);

                    break;
                }
            }
        };

        nextButton.setOnClickListener(newListener);
        previousButton.setOnClickListener(newListener);
        return V;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
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
