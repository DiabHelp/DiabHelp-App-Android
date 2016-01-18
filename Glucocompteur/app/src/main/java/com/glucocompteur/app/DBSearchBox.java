package com.glucocompteur.app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by sundava on 03/12/15.
 */
public class DBSearchBox extends AutoCompleteTextView {

    DBHelper dbHelper;
    private ArrayList<Aliment> alimentList;
    Vector<MyObject> products;

    public DBSearchBox(android.content.Context context , android.util.AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    public void initDBHooks(DBHelper dbHelper)
    {
        this.dbHelper = dbHelper;
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String[] alimList = getAlimentsFromPattern(s);
                setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, alimList));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public String[] sortItems(String [] items, String pattern)
    {
        int size = items.length;
        String[] res = new String[size];
        ArrayList <String> wordList = new ArrayList<>();
        for (String item : items)
        {
            wordList.add(item);
        }
        int index = 0;
        int i = 0;
        for (Iterator<String> iterator = wordList.iterator(); iterator.hasNext();)
        {
            String word = iterator.next();
            String[] words = word.replace(",", "").split(" ");
            if (words[0].equals(pattern) || words[0].contains(pattern)) {
                res[index] = word;
                iterator.remove();
                index++;
            }
            i++;
        }
        for (Iterator<String> iterator = wordList.iterator(); iterator.hasNext();)
        {
            String item = iterator.next();
            res[index] = item;
            index++;
            iterator.remove();
        }
        Log.d("DBSort", "Sorting items :");
        for (String item : items)
        {
            Log.d("DBSort", item);

        }
        Log.d("DBSort", "result :");
        for (String item : res)
        {
            Log.d("DBSort", "res : " + item);

        }
        return res;

    }

    public String[] getAlimentsFromPattern(CharSequence cs)
    {
        String pattern = cs.toString();
        products = dbHelper.search(pattern);
        int rowCount = products.size();
        String[] item = new String[rowCount];
        int x = 0;
        for (MyObject record : products) {
            Log.d("DBSearch", "item[" + x + "]  == [" + record.objectName + "]");
            item[x] = record.objectName;
            x++;
        }

        return item;
    }

    public void setAlimentList(ArrayList<Aliment> alimentList) {
        this.alimentList = alimentList;
    }
}
