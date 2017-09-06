package fr.diabhelp.diabhelp.Glucocompteur;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sundava on 03/12/15.
 */
public class DBSearchBox extends AppCompatAutoCompleteTextView {

    DBHelper dbHelper;
    Vector<MyObject> products;
    private ArrayList<Aliment> alimentList;

    public DBSearchBox(Context context , AttributeSet attributeSet)
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
