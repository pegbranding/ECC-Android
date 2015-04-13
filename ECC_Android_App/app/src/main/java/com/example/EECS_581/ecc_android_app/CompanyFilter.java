package com.example.EECS_581.ecc_android_app;

import android.widget.Filter;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by eric on 4/13/15.
 */
public class CompanyFilter extends Filter {
    private CompanyArrayAdapter companyAdapter;
    private List<Company> allValues;
    private List<Company> filteredValues;

    public CompanyFilter(CompanyArrayAdapter cadapter) {
        this.companyAdapter = cadapter;
        this.allValues = companyAdapter.getValues();
        this.filteredValues = companyAdapter.getFilteredValues();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults result = new FilterResults();
        constraint = constraint.toString().toLowerCase();

        if (constraint == null || constraint.length() == 0) {
            ArrayList<Company> list = new ArrayList<Company>(allValues);
            result.values = allValues;
            result.count = allValues.size();
        } else {
            final ArrayList<Company> original = new ArrayList<Company>(allValues);
            final ArrayList<Company> filtered = new ArrayList<Company>();

            for (int i = 0; i < original.size(); i++) {
                final Company c = original.get(i);
                if (c.getName().toLowerCase().contains(constraint)) {
                    filtered.add(c);
                }
            }
            result.values = filtered;
            result.count = filtered.size();
        }
        return result;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredValues = (List<Company>) results.values;
        companyAdapter.notifyDataSetChanged();
        companyAdapter.clear();

        for (int i = 0; i < filteredValues.size(); i++) {
            companyAdapter.add(filteredValues.get(i));
            companyAdapter.notifyDataSetInvalidated();
        }
    }

}
