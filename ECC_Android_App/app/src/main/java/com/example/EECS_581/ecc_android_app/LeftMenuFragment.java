package com.example.EECS_581.ecc_android_app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Jiaxiang Li on 2/15/2015.
 */
public class LeftMenuFragment extends Fragment {
    private View mView;
    private Context mContext;
    private ListView listView_left_category;
    //private LeftCateGoryAdapter mAdapter;
    private String[] category_name;
    private String[] category_title;
    private Integer[] category_img;
    //private List<ItemCategoryModel> mLists;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle saveInstanceState){
        if(mView==null){
            mView=inflater.inflate(R.layout.leftmenu,container,false);
            initView();
            initValidata();
            //bindData();
            //initListerer();
        }

        return mView;
    }

    private void initView(){
        listView_left_category=(ListView) mView.findViewById(R.id.listView_left_category);

    }


    private void initValidata(){

    }


}
