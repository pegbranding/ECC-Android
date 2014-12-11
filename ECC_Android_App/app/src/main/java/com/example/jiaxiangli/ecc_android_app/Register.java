package com.example.jiaxiangli.ecc_android_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Jiaxiang Li on 11/25/2014.
 */
public class Register extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
    }
}
