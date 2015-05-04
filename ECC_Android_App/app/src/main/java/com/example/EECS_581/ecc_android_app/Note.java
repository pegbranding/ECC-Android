package com.example.EECS_581.ecc_android_app;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/**
 * Created by eric on 4/27/15.
 */
public class Note implements Parcelable {
    String title;
    String body;

    String company_name;
    String type;

    public Note() {title = "New Note";}

    public Note(Parcel in) {
        String[] data = new String[4];
        this.title = data[0];
        this.body = data[1];
        this.company_name = data[2];
        this.type = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.title, this.body, this.company_name, this.type});
    }

    public Note(String ti, String b, String cn, String ty) {
        title = ti;
        body = b;
        company_name = cn;
        type = ty;
    }

    public String getTitle() {return title;}
    public void setTitle(String t) {title = t;}

    public String getBody() {return body;}
    public void setBody(String b) { body = b; }

    public String getCompanyName() {return company_name;}
    public void setCompanyName(String c) {company_name = c;}

    public String getType() {return type;}
    public void setType(String t) { type = t;}

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("title", title);
            json.put("body", body);
            json.put("company_name", company_name);
            json.put("type", type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return json;
    }
}
