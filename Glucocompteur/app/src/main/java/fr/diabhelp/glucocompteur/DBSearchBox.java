package fr.diabhelp.glucocompteur;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by sundava on 03/12/15.
 */
public class DBSearchBox extends AutoCompleteTextView {

    DBHelper dbHelper;
    Vector<MyObject> products;
    private ArrayList<Aliment> alimentList;

    public DBSearchBox(android.content.Context context , android.util.AttributeSet attributeSet)
    {
        super(context, attributeSet);
    }

    public void initDBHooks(DBHelper dbHelper)
    {
        this.dbHelper = dbHelper;
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getContext(), dbHelper );
        this.setAdapter(adapter);
    }



    // Junk si je reussis a faire marcher l'adapter
    // Ca marche mais on garde quand meme parce que j'ai peur sarace
    /*
    public String[] getAlimentsFromPattern(CharSequence cs)
    {
        String pattern = cs.toString();
        products = dbHelper.search(pattern);
        int rowCount = products.size();
        Log.d("DBSearchBox", "Product list size = " + rowCount);
        String[] item = new String[rowCount];
        int x = 0;
        for (MyObject record : products) {
            Log.d("DBSearchBox", "item[" + x + "]  == [" + record.objectName + "]");
            item[x] = record.objectName;
            x++;
        }

        return item;
    }
    */

    public void setAlimentList(ArrayList<Aliment> alimentList) {
        this.alimentList = alimentList;
    }

    public MyObject getObject(int position) {
        return (MyObject)getAdapter().getItem(position);
    }
}
