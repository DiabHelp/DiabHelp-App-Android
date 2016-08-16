package fr.diabhelp.glucocompteur;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

/**
 * Created by Sundava on 16/08/2016.
 */
public class AutoCompleteAdapter extends ArrayAdapter<MyObject> implements Filterable {
    private final DBHelper dbHelper;
    private final LayoutInflater mInflater;
    Vector<MyObject> products;

    public AutoCompleteAdapter(Context context, DBHelper dbHelper) {
        super(context, -1);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbHelper = dbHelper;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView tv;
        if (convertView != null) {
            tv = (TextView) convertView;
        } else {
            tv = (TextView) mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        tv.setText(getItem(position).getObjectName());
        return tv;
    }

    @Override
    public Filter getFilter()
    {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence cs) {
                String pattern = "";
                if (cs != null)
                    pattern = cs.toString();
                products = dbHelper.search(pattern);
                Log.d("AutoCompleteAdapter", "Product list size = " +  products.size());
                final FilterResults filterResults = new FilterResults();
                filterResults.values = products;
                filterResults.count =  products.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                if (filterResults.count > 0) {
                    for (MyObject object : (List<MyObject>) filterResults.values)
                        add(object);
                }
                notifyDataSetChanged();
            }
            @Override
            public CharSequence convertResultToString(final Object resultValue) {
                String name = "";
                if (resultValue != null)
                {
                     name =  ((MyObject) resultValue).getObjectName();
                }
                Log.d("AutoCompleteAdapter", "convertResultToString : " + name);
                return  name;
            }
        };
        return myFilter;
    }
}
