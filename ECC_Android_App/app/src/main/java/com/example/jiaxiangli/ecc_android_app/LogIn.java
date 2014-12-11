package com.example.jiaxiangli.ecc_android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class LogIn extends Activity implements View.OnClickListener {
    private Button logInBtn;
    private TextView signUpLnk;
    private TextView getPassLnk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        logInBtn=(Button) findViewById(R.id.login_btn);
        signUpLnk=(TextView) findViewById(R.id.register_link);
        getPassLnk=(TextView) findViewById(R.id.forgotPass_link);

        logInBtn.setOnClickListener(this);
        signUpLnk.setOnClickListener(this);
        getPassLnk.setOnClickListener(this);


    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.login_btn:
                Intent homePageAct=new Intent(v.getContext(),HomePage.class);
                startActivity(homePageAct);
                break;

            case R.id.forgotPass_link:
                Intent getPasswordAct=new Intent(v.getContext(),GetPassword.class);
                startActivity(getPasswordAct);
                break;

            case R.id.register_link:
                Intent regAct=new Intent(v.getContext(),Register.class);
                startActivity(regAct);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
