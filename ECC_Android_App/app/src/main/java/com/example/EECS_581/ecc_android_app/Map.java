package com.example.EECS_581.ecc_android_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.ViewGroup.LayoutParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;


public class Map extends Fragment {
    private String urlstring = new String("https://engr.drupal.ku.edu/sites/engr.drupal.ku.edu/files/files/png_maps.txt");
    ArrayList<String> urlarraylist = new ArrayList();
    View view;
    ViewFlipper ECCFlipper;

    private Handler mHandler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_map, container, false);
        //sets the original progress for the progressbar depending on which image it is looking at.
        ECCFlipper = (ViewFlipper)view.findViewById(R.id.eccflipper);

        new CallingUrls().execute();

        //sets up the calls for the buttons in order to use later
        Button nextButton = (Button)view.findViewById(R.id.nextbutton);
        Button previousButton = (Button)view.findViewById(R.id.previousbutton);

        DisplayImageOptions defaultoptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(view.getContext())
                .defaultDisplayImageOptions(defaultoptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);

        //sets up progress bar and next/previous buttons
        ProgressBar eccprogress2 = (ProgressBar) view.findViewById(R.id.progressBar);
        int oldprogress;
        if(ECCFlipper.getChildCount() == 1)
            oldprogress = 0;
        else
            oldprogress = ((ECCFlipper.getDisplayedChild() * 100) / (ECCFlipper.getChildCount() - 1));
        eccprogress2.setProgress(oldprogress);

        View.OnClickListener newListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.nextbutton:

                        final Toast toast = Toast.makeText(view.getContext(), "Next Map", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 500);
                        //Finds the ViewFlipper and goes to the next image
                        ECCFlipper.setInAnimation(view.getContext(), android.R.anim.fade_in);
                        ECCFlipper.setOutAnimation(view.getContext(), android.R.anim.fade_out);
                        ECCFlipper.showNext();
                        //incrementing the progressbar
                        ProgressBar eccprogress = (ProgressBar) view.findViewById(R.id.progressBar);
                        int oldprogress;
                        if(ECCFlipper.getChildCount() == 1)
                            oldprogress = 0;
                        else
                            oldprogress= ((ECCFlipper.getDisplayedChild() * 100) / (ECCFlipper.getChildCount() - 1));
                        eccprogress.setProgress(oldprogress);

                        break;
                    case R.id.previousbutton:

                        final Toast toast2 = Toast.makeText(view.getContext(), "Previous Map", Toast.LENGTH_SHORT);
                        toast2.show();
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast2.cancel();
                            }
                        }, 500);
                        //Finds the ViewFlipper and goes to the previous image
                        ECCFlipper.setInAnimation(view.getContext(), android.R.anim.fade_in);
                        ECCFlipper.setOutAnimation(view.getContext(), android.R.anim.fade_out);
                        ECCFlipper.showPrevious();
                        //decrementing the progressbar
                        ProgressBar eccprogress2 = (ProgressBar) view.findViewById(R.id.progressBar);
                        int oldprogress2;
                        if(ECCFlipper.getChildCount() == 1)
                            oldprogress2 = 0;
                        else
                            oldprogress2 = ((ECCFlipper.getDisplayedChild() * 100) / (ECCFlipper.getChildCount() - 1));
                        eccprogress2.setProgress(oldprogress2);

                        break;
                }
            }
        };

        nextButton.setOnClickListener(newListener);
        previousButton.setOnClickListener(newListener);

        return view;
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

    //runs all map gathering from an asynctask
    private class CallingUrls extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params){
            try {
                URL url = new URL(urlstring);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while((str = in.readLine()) != null)
                {
                    urlarraylist.add(str);
                }
                in.close();
            }catch(MalformedURLException e) {//System.out.println("Malformed URL in setting images");
                return e.getMessage();}
            catch(IOException e) {//System.out.println("IOException in setting images");
                System.out.println(e.getMessage());
                return e.getMessage();}
            return "";
        }

        //pulls the maps from the given urls, and stores them in imageviews
        protected void onPostExecute(String result){
            ImageViewTouch image1view = (ImageViewTouch) view.findViewById(R.id.image1);
            ViewGroup.LayoutParams vp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            //setting up imageviewtouch with parameters to be able to input image
            if(urlarraylist.size() >= 1 ) {
                System.out.println(urlarraylist.get(0));
                ImageViewTouch[] imageviewtoucharray = new ImageViewTouch[urlarraylist.size() - 1];
                for (int j = 0; j < (urlarraylist.size() - 1); j++) {
                    imageviewtoucharray[j] = new ImageViewTouch(getActivity(), null);
                    imageviewtoucharray[j].setLayoutParams(vp);
                }

                //adding the imageviewtouch to the viewflipper
                for (int k = 0; k < (urlarraylist.size() - 1); k++) {
                    ECCFlipper.addView(imageviewtoucharray[k]);
                }

                //setting up imageloader to input the images from the urls to the imageviewtouch views
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(urlarraylist.get(0), image1view);
                for (int i = 1; i < urlarraylist.size(); i++) {
                    imageLoader.displayImage(urlarraylist.get(i), imageviewtoucharray[i - 1]);
                }
            }
        }
    }
}