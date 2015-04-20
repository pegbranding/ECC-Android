package com.example.EECS_581.ecc_android_app;



import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import java.util.ArrayList;

import adapter.NavDrawerListAdapter;
import entity.NavDrawerItem;
import android.widget.AdapterView.OnItemClickListener;




/**
 * Created by Jiaxiang Li on 4/18/2015.
 */
public class MenuFragment extends Fragment {

    private ListView mDrawerList;
    private String[] mNavMenuTitles;
    private TypedArray mNavmenuIconsTypeArray;
    private ArrayList<NavDrawerItem> mNavDrawerItems;
    private NavDrawerListAdapter mAdapter;

    public void onAttach(Activity activity){
        /*try{
            mCallBack(SLMenuListOnItemClickListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnResolveTelsCompletedListener");
        }*/
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.leftmenufragment,null);
        findView(rootView);
        return rootView;
    }

    private void findView(View rootView){
        mDrawerList=(ListView)rootView.findViewById(R.id.leftmenu_list);
        mNavMenuTitles=getResources().getStringArray(R.array.nav_items);
        mNavmenuIconsTypeArray=getResources().obtainTypedArray(R.array.nav_icons);

        mNavDrawerItems=new ArrayList<NavDrawerItem>();

        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavmenuIconsTypeArray
                .getResourceId(0, -1)));

        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavmenuIconsTypeArray
                .getResourceId(1, -1)));

        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavmenuIconsTypeArray
                .getResourceId(2, -1)));

        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavmenuIconsTypeArray
                .getResourceId(3, -1)));

        mNavmenuIconsTypeArray.recycle();

        mAdapter=new NavDrawerListAdapter(getActivity(),mNavDrawerItems);

        mDrawerList.setAdapter(mAdapter);


        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemPosition = i;
                System.out.println("Selected: " + itemPosition);


                if (itemPosition == 1) {
                    CompanyList.cadapter.getFilter().filter("");
                } else if (itemPosition == 2) {
                    CompanyList.cadapter.getFilter().filter("favorites");
                } else if (itemPosition == 3) {
                    CompanyList.cadapter.getFilter().filter("visited");
                }
            }
        });

    }

}
