package com.example.EECS_581.ecc_android_app;

/**
 * Created by eric on 4/6/15.
 */
public class Company {
    String company_name;
    String tableNum;
    String label;

    public Company() {
        company_name = "";
    }

    public Company(String cn, String tn) {
        company_name = cn;
        tableNum = tn;
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

    public void setTableNum(String t) {
        tableNum = t;
    }

    public String getLabel() {
        return "This is a test label";
    }

}
