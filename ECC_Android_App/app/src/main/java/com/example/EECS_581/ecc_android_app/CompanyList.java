package com.example.EECS_581.ecc_android_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.AsyncTask;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.json.JSONObject;
import org.json.JSONArray;

import static com.example.EECS_581.ecc_android_app.R.layout.activity_company_list;


public class CompanyList extends Fragment {
    private String restURL = "http://54.149.119.218:28017/companylist/fall2014/";

    ArrayList<String> companyNameList = new ArrayList<String>();
    private boolean setupList = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(activity_company_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.companyListView);

        if (!setupList) {
            companyNameList.add("AAAA");
            companyNameList.add("BBBB");
            Log.d("MyApp", "@@@@@@@@@@@@@@@@@@@@@@@@@@");
            setupList = true;
        }

        new CallAPI().execute();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, companyNameList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemPosition = i;
                String itemValue = (String) companyNameList.get(itemPosition);

                Toast.makeText(getActivity().getApplicationContext(), "Position:"+itemPosition+" ListItem :"+itemValue,
                    Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_company_list, menu);
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

    private class CallAPI extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedInputStream bufferedStream;

            try {
                URL url = new URL(restURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                bufferedStream = new BufferedInputStream(urlConnection.getInputStream());

                InputStreamReader reader = new InputStreamReader(bufferedStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }

                String result = builder.toString();
                //System.out.println("LINE: " + result);
                JSONObject topLevel = new JSONObject(result);

                ArrayList<JSONObject> companiesList = new ArrayList<JSONObject>();

                JSONArray arr = topLevel.getJSONArray("rows");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject curCompany = (JSONObject) arr.get(i);
                    String curCompanyName =  ((String) curCompany.get("company_name")).substring(1);
                    System.out.println("ThisCompany Name; " + curCompany.get("company_name"));
                }



            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }

            return "";
        }

        protected void onPostExecute(String result) {

        }
    }
}
