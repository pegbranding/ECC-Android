package com.example.EECS_581.ecc_android_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import org.json.JSONObject;
import android.widget.TextView;
import android.view.View;
import android.text.Html;


public class CompanyDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String st = i.getStringExtra("Company_JSON_Object");
        System.out.println("Got intent with data: " + st);

        setContentView(R.layout.activity_company_detail);

        try {
            JSONObject j = new JSONObject(st);
            String companyName = (String) j.get("company_name");
            companyName = companyName.substring(1);
            setTitle(companyName);

            TextView displayCompanyName = (TextView) findViewById(R.id.companyDetailName);
            displayCompanyName.append(companyName);

            TextView displayCompanyECCTable = (TextView) findViewById(R.id.companyDetailECCTable);
            String ECC_table = (String) j.get("ecc_table");
            ECC_table = ECC_table.substring(1);
            displayCompanyECCTable.append(ECC_table);

            TextView displayCompanyOverview = (TextView) findViewById(R.id.companyDetailOverview);
            String overview = (String) j.get("overview");
            overview = overview.substring(1);
            displayCompanyOverview.append(Html.fromHtml(overview));

            TextView displayCompanyWebsite = (TextView) findViewById(R.id.companyDetailWebsite);
            String website = (String) j.get("website");
            website = website.substring(1);
            displayCompanyWebsite.append(website);

            TextView displayCompanyMajors = (TextView) findViewById(R.id.companyDetailMajors);
            String majors = (String) j.get("majors");
            majors = majors.substring(1);
            displayCompanyMajors.append(majors);

            TextView displayCompanyPositionTypes = (TextView) findViewById(R.id.companyDetailPositionTypes);
            String position_types = (String) j.get("position_types");
            displayCompanyPositionTypes.append(position_types);

            TextView displayCompanyDegreeLevels = (TextView) findViewById(R.id.companyDetailDegreeLevels);
            String degree_levels = (String) j.get("degree_levels");
            degree_levels = degree_levels.substring(1);
            displayCompanyDegreeLevels.append(degree_levels);

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
