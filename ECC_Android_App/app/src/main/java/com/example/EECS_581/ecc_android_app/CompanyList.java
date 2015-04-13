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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.AsyncTask;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.content.Intent;
import java.io.File;
import android.content.Context;
import android.widget.SearchView;


import org.json.JSONObject;
import org.json.JSONArray;
import android.text.TextWatcher;
import android.text.Editable;

import static com.example.EECS_581.ecc_android_app.R.layout.activity_company_list;


public class CompanyList extends Fragment {
    private String restURL = "http://54.149.47.206:28017/companylist/fall2014/";

    public static ArrayList<Company> companyList = new ArrayList<Company> ();

    View view;

    EditText inputSearch;
    public static CompanyArrayAdapter cadapter;
    ListView listview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(activity_company_list, container, false);
        listview = (ListView) view.findViewById(R.id.companyListView);
        inputSearch = (EditText) view.findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                CompanyList.this.cadapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        new CallAPI().execute();

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

    public static void setVisited(String st, boolean b) {
        for (int i = 0; i < companyList.size(); i++) {
            if (companyList.get(i).getName().equals(st)) {
                companyList.get(i).setVisited(b);
                cadapter.notifyDataSetChanged();
            }
        }
    }

    public static void setFavorited(String st, boolean b) {
        for (int i = 0; i < companyList.size(); i++) {
            if (companyList.get(i).getName().equals(st)) {
                companyList.get(i).setFavorited(b);
                cadapter.notifyDataSetChanged();
            }
        }
    }

    private class CallAPI extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedInputStream bufferedStream;

            try {
                File file = getActivity().getBaseContext().getFileStreamPath("companylist");
                if (file.exists()) {
                    System.out.println("It exists!");
                    FileInputStream fis = new FileInputStream(file);
                    bufferedStream = new BufferedInputStream(fis);
                } else {

                    System.out.println("Does not exist!");

                    URL url = new URL(restURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    bufferedStream = new BufferedInputStream(urlConnection.getInputStream());

                }

                InputStreamReader reader = new InputStreamReader(bufferedStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                String result = new String(builder.toString());
                System.out.println("result: " + result);
                //System.out.println("LINE: " + result);
                if (companyList.size() == 0) {
                    JSONObject topLevel = new JSONObject(result);

                    JSONArray arr = topLevel.getJSONArray("rows");

                    for (int i = 1; i < arr.length(); i++) {
                        JSONObject curCompany = (JSONObject) arr.get(i);
                        String curCompanyName = ((String) curCompany.get("company_name")).substring(1);
                        String curCompanyTableNum = ((String) curCompany.get("ecc_table")).substring(1);
                        String curCompanyOverview = ((String) curCompany.get("overview")).substring(1);
                        String curCompanyWebsite = ((String) curCompany.get("website")).substring(1);
                        String curCompanyMajors = ((String) curCompany.get("majors")).substring(1);
                        String curCompanyPositions = ((String) curCompany.get("position_types"));
                        String curCompanyDegrees = ((String) curCompany.get("degree_levels")).substring(1);

                        Company curr = new Company(curCompanyName, curCompanyTableNum, curCompanyOverview,
                                curCompanyWebsite, curCompanyMajors, curCompanyPositions, curCompanyDegrees);

                        companyList.add(curr);
                    }

                    sortCompanies(companyList);


                }

                if (!file.exists()) {
                    FileOutputStream fos = getActivity().getBaseContext().openFileOutput("companylist", Context.MODE_PRIVATE);
                    byte[] content = result.getBytes();
                    fos.write(content);
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }

            return "";
        }

        public void sortCompanies(ArrayList<Company> list) {
            for (int x = 0; x < list.size(); x++) {
                for (int y = 0; y < list.size(); y++) {
                    if (list.get(x).getName().compareTo(list.get(y).getName()) < 0) {
                        Company temp = list.get(x);
                        list.set(x, list.get(y));
                        list.set(y, temp);
                    }
                }
            }
        }

        public int findIndex(ArrayList<Company> list, String nameToFind) {
            int index = -1;
            for (int x = 0; x < list.size(); x++) {
                if (list.get(x).getName().equals(nameToFind)) {
                    index = x;
                    break;
                }
            }
            return index;
        }

        protected void onPostExecute(String result) {
            ListView listView = (ListView) view.findViewById(R.id.companyListView);

            cadapter = new CompanyArrayAdapter(getActivity(), companyList);

            listView.setAdapter(cadapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int itemPosition = i;
                    String itemValue = companyList.get(itemPosition).getName();

                    int index = findIndex(companyList, itemValue);

                    try {
                        JSONObject curCompany = (JSONObject) companyList.get(index).toJSON();
                        //System.out.println("Value in detailedData: " + ((String) curCompany.get("company_name")));
                        Intent newActivity = new Intent(getActivity(), CompanyDetail.class);
                        newActivity.putExtra("Company_JSON_Object", curCompany.toString());

                        startActivity(newActivity);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

        }
    }
}
