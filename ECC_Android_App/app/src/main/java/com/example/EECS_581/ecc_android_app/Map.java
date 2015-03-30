package com.example.EECS_581.ecc_android_app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class Map extends Fragment {
    String image1path = "http://people.eecs.ku.edu/~gbusing/floor4.png";
    String image2path = "http://people.eecs.ku.edu/~gbusing/ballroomfloor5.png";
    String image3path = "http://people.eecs.ku.edu/~gbusing/big12floor5.png";
    String image4path = "http://people.eecs.ku.edu/~gbusing/kansasroomfloor6.png";
    String image5path = "http://people.eecs.ku.edu/~gbusing/malottroomfloor6.png";

    private Handler mHandler = new Handler();
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
        ImageView image1view = (ImageView)V.findViewById(R.id.image1);
        ImageView image2view = (ImageView)V.findViewById(R.id.image2);
        ImageView image3view = (ImageView)V.findViewById(R.id.image3);
        ImageView image4view = (ImageView)V.findViewById(R.id.image4);
        ImageView image5view = (ImageView)V.findViewById(R.id.image5);

        ImageView[] imageviewarray = {image1view, image2view, image3view, image4view, image5view};
        String[] imagepatharray = {image1path, image2path, image3path, image4path, image5path};

        DisplayImageOptions defaultoptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(V.getContext())
                .defaultDisplayImageOptions(defaultoptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

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

        ImageLoader imageLoader = ImageLoader.getInstance();
        for(int i = 0; i < imageviewarray.length; i++)
        {
            imageLoader.displayImage(imagepatharray[i], imageviewarray[i]);
        }

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