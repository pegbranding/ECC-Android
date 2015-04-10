package com.example.EECS_581.ecc_android_app;

import org.json.JSONObject;
import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by eric on 4/6/15.
 */
public class Company implements Parcelable {
    String company_name;
    String tableNum;
    String overview;
    String website;
    String majors;
    String position_types;
    String degree_levels;

    boolean favorited;
    boolean visited;


    public Company() {
        company_name = "";
    }

    public Company(Parcel in) {
        String[] data = new String[9];
        this.company_name = data[0];
        this.tableNum = data[1];
        this.overview = data[2];
        this.website = data[3];
        this.majors = data[4];
        this.position_types = data[5];
        this.degree_levels = data[6];
        this.favorited = Boolean.parseBoolean(data[7]);
        this.visited = Boolean.parseBoolean(data[8]);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.company_name, this.tableNum, this.overview, this.website, this.majors,
                this.position_types, this.degree_levels, String.valueOf(this.favorited), String.valueOf(this.visited)});
    }


    public Company(String cn, String tn, String ov, String ws, String maj, String pos, String deg) {
        company_name = cn;
        tableNum = tn;
        overview = ov;
        website = ws;
        majors = maj;
        position_types = pos;
        degree_levels = deg;


        favorited = false;
        visited = false;
    }

    public String getName() {
        return company_name;
    }

    public void setName(String n) {
        company_name = n;
    }

    public String getTableNum() {
        return tableNum;
    }

    public boolean getFavorited() { return favorited; }

    public boolean getVisited() { return visited; }

    public void setTableNum(String t) {
        tableNum = t;
    }

    public void setVisited(boolean b) { visited = b; }

    public void setFavorited(boolean b) { favorited = b; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("company_name", company_name);
            json.put("ecc_table", tableNum);
            json.put("overview", overview);
            json.put("website", website);
            json.put("majors", majors);
            json.put("position_types", position_types);
            json.put("degree_levels", degree_levels);
            json.put("favorited", favorited);
            json.put("visited", visited);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return json;
    }


}
