package com.example.EECS_581.ecc_android_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import org.json.JSONObject;

import android.widget.CompoundButton;
import android.widget.TextView;
import android.view.View;
import android.text.Html;
import android.widget.CheckBox;


public class CompanyDetail extends Activity {

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String st = i.getStringExtra("Company_JSON_Object");
        System.out.println("Got intent with data: " + st);

        setContentView(R.layout.activity_company_detail);

        updateCompanyInfo(st);



    }

    public void updateCompanyInfo(String st) {
        try {
            JSONObject j = new JSONObject(st);
            final String companyName = (String) j.get("company_name");
            setTitle(companyName);

            TextView displayCompanyName = (TextView) findViewById(R.id.companyDetailName);
            displayCompanyName.append(companyName);

            TextView displayCompanyECCTable = (TextView) findViewById(R.id.companyDetailECCTable);
            String ECC_table = (String) j.get("ecc_table");
            displayCompanyECCTable.append(ECC_table);

            TextView displayCompanyOverview = (TextView) findViewById(R.id.companyDetailOverview);
            String overview = (String) j.get("overview");
            displayCompanyOverview.append(Html.fromHtml(overview));

            TextView displayCompanyWebsite = (TextView) findViewById(R.id.companyDetailWebsite);
            String website = (String) j.get("website");
            displayCompanyWebsite.append(website);

            TextView displayCompanyMajors = (TextView) findViewById(R.id.companyDetailMajors);
            String majors = (String) j.get("majors");
            displayCompanyMajors.append(majors);

            TextView displayCompanyPositionTypes = (TextView) findViewById(R.id.companyDetailPositionTypes);
            String position_types = (String) j.get("position_types");
            displayCompanyPositionTypes.append(position_types);

            TextView displayCompanyDegreeLevels = (TextView) findViewById(R.id.companyDetailDegreeLevels);
            String degree_levels = (String) j.get("degree_levels");
            displayCompanyDegreeLevels.append(degree_levels);

            CheckBox visitedBox = (CheckBox) findViewById (R.id.companyDetailVisited);
            Boolean visited = (Boolean) j.get("visited");
            visitedBox.setChecked(visited);
            visitedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    System.out.println("visited check is now: " + isChecked);
                    CompanyList.setVisited(companyName, isChecked);
                }
            });

            CheckBox favoritedBox = (CheckBox) findViewById (R.id.companyDetailFavorite);
            Boolean favorited = (Boolean) j.get("favorited");
            favoritedBox.setChecked(favorited);
            favoritedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    System.out.println("favorite check is now: " + isChecked);
                    CompanyList.setFavorited(companyName, isChecked);
                }
            });


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_detail, menu);
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
}
