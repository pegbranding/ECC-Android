package com.example.EECS_581.ecc_android_app;

import android.widget.ArrayAdapter;
import android.widget.Filterable;
import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import java.util.ArrayList;
import java.util.logging.Filter;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by eric on 4/6/15.
 */


public class CompanyArrayAdapter extends ArrayAdapter<Company> implements Filterable{

    private ArrayList<Company> allCompanyItemsArray;
    private ArrayList<Company> filteredCompanyItemsArray;
    private Activity context;
    //private ModelFilter filter;
    private LayoutInflater inflater;


    public CompanyArrayAdapter(Activity context, List<Company> list) {
        super(context, R.layout.company_list_row_item, list);
        System.out.println("Size of given list: " + list.size());
        this.context = context;
        this.allCompanyItemsArray = new ArrayList<Company>();
        allCompanyItemsArray.addAll(list);
        this.filteredCompanyItemsArray = new ArrayList<Company>();
        filteredCompanyItemsArray.addAll(allCompanyItemsArray);
        inflater = context.getLayoutInflater();
        //getFilter();
    }

    /*@Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new ModelFilter();
        }
        return filter;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("size of our list: " + allCompanyItemsArray.size());
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.company_list_row_item, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.tableNum);

        // 4. Set the text for textView
        labelView.setText(allCompanyItemsArray.get(position).getName());
        valueView.setText(allCompanyItemsArray.get(position).getTableNum());


        // 5. retrn rowView
        return rowView;

       /* View view = null;

        Company c = filteredCompanyItemsArray.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {

            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.companyDetailName);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }
        viewHolder.text.setText(c.getName());

        return view;*/
    }

    /*private class ModelFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Model> filteredItems = new ArrayList<Model>();

                for(int i = 0, l = allModelItemsArray.size(); i < l; i++)
                {
                    Model m = allModelItemsArray.get(i);
                    if(m.getName().toLowerCase().contains(constraint))
                        filteredItems.add(m);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = allModelItemsArray;
                    result.count = allModelItemsArray.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredModelItemsArray = (ArrayList<Model>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = filteredModelItemsArray.size(); i < l; i++)
                add(filteredModelItemsArray.get(i));
            notifyDataSetInvalidated();
        }
    }*/
}