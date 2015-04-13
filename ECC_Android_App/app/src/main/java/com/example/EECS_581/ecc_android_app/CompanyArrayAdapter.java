package com.example.EECS_581.ecc_android_app;

import android.widget.Filter;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by eric on 4/6/15.
 */


public class CompanyArrayAdapter extends ArrayAdapter implements Filterable{

    public static ArrayList<Company> allCompanyItemsArray;
    public static ArrayList<Company> filteredCompanyItemsArray;
    private CompanyFilter mFilter;

    private Activity context;
    private LayoutInflater inflater;


    public CompanyArrayAdapter(Activity context, List<Company> list) {
        super(context, R.layout.company_list_row_item, list);
        this.context = context;
        this.allCompanyItemsArray = new ArrayList<Company>();
        allCompanyItemsArray.addAll(list);
        this.filteredCompanyItemsArray = new ArrayList<Company>();
        filteredCompanyItemsArray.addAll(allCompanyItemsArray);
        this.mFilter = new CompanyFilter(this);
        inflater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.company_list_row_item, parent, false);
        }

        Company c = CompanyList.companyList.get(position);

        if (c != null) {
            TextView labelView = (TextView) rowView.findViewById(R.id.label);
            TextView valueView = (TextView) rowView.findViewById(R.id.tableNum);

            if (labelView != null) {
                labelView.setText(CompanyList.companyList.get(position).getName());
            }
            if (valueView != null) {
                valueView.setText(CompanyList.companyList.get(position).getTableNum());
            }
        }

        return rowView;
    }

    public List<Company> getValues() {
        return allCompanyItemsArray;
    }

    public List<Company> getFilteredValues() {
        return filteredCompanyItemsArray;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CompanyFilter(this);
        }
        return mFilter;
    }

}