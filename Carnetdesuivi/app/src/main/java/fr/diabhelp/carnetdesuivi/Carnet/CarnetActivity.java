package fr.diabhelp.carnetdesuivi.Carnet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.diabhelp.carnetdesuivi.DataBase.DAO;
import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.R;

/**
 * Created by naqued on 10/11/15.
 */
public class CarnetActivity extends AppCompatActivity {
    public GridView grid;
    public GridView gridba;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    static DAO bdd;

    //Expandable
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    public enum InputType{
        GLUCIDE(0),
        FAST_INSU(1),
        SLOW_INSU(2),
        HBA1C(3),
        GLYCEMY(4);

        private final int value;

        private InputType(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carnet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bdd = new DAO(this);
        TextView noEntry = (TextView) findViewById(R.id.Noentry);

        createGroupList();
        createCollection();


        if (groupList == null)
        {
            noEntry.setEnabled(true);
            noEntry.setVisibility(View.VISIBLE);
            return ;
        }

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        final ExpandableListAdapters expListAdapter = new ExpandableListAdapters(
                this, groupList, laptopCollection);
        expListView.setAdapter(expListAdapter);
        if (groupList != null && groupList.size() > 0)
        {
            noEntry.setEnabled(false);
            noEntry.setVisibility(View.INVISIBLE);
            expListView.expandGroup(0);
            if (groupList.size() > 1)
                expListView.expandGroup(1);
            if (groupList.size() > 2)
                expListView.expandGroup(2);
        }

        //setGroupIndicatorToRight();
/*        expListView.expandGroup(1); // expend le numéro 1*/
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList = fillDate();
        Collections.reverse(groupList);

/*        groupList.add("HP");
        groupList.add("Dell");
        groupList.add("Lenovo");
        groupList.add("Sony");
        groupList.add("HCL");
        groupList.add("Samsung");*/
    }

    private void createCollection() {
        // preparing laptops collection(child)

        laptopCollection = new LinkedHashMap<String, List<String>>();
        if (groupList != null) {
            bdd.open();
            for (String date : groupList) {
                loadChild(fillValueDay(date.split(" ")[0], date.split(" ")[1]));

                laptopCollection.put(date, childList);
            }
            bdd.close();
        }
    }

    public void launch_view_day(View v)
    {
        Intent dayvieintent = new Intent(CarnetActivity.this, DayResultActivity.class);
        CarnetActivity.this.startActivity(dayvieintent);
    }

    public void launch_entry(View v) {
        Intent Entryintent = new Intent(CarnetActivity.this, EntryActivity.class);
        CarnetActivity.this.startActivity(Entryintent);
        this.finish();
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    protected ArrayList<TextView> fill_inputchild()
    {
        ArrayList<TextView> infView = new ArrayList<TextView>(12);
        View convertView;

            LayoutInflater infalInflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);


        infView.add(InputType.GLUCIDE.getValue(), (TextView) convertView.findViewById(R.id.glutextview));

        infView.add(InputType.FAST_INSU.getValue(), (TextView) convertView.findViewById(R.id.fasttextView));
        infView.add(InputType.SLOW_INSU.getValue(), (TextView) convertView.findViewById(R.id.slowtextView));

        infView.add(InputType.HBA1C.getValue(), (TextView) convertView.findViewById(R.id.hba1ctextView));
        infView.add(InputType.GLYCEMY.getValue(), (TextView) convertView.findViewById(R.id.glytextview));

        return infView;
    }

    protected String[] fillValueDay(String dateVal, String hour) {
        String value[] = new String[1]; //TODO a faire mieux
        return value;
    }

    protected ArrayList<String> fillDate() {
        ArrayList<String> date = new ArrayList<String>(); //TODO a faire mieux
        ArrayList<EntryOfCDS> mall = null;

        int today[] = getDate();
        int idx = 0;

        int iYear = 2015; //TODO attention a rendre dynamique
        int iMonth = Calendar.FEBRUARY;
        int iDay = 1;

        Calendar mycal = new GregorianCalendar(iYear, today[1], iDay);


        // Get the number of days in that month
        bdd.open();
        mall = bdd.SelectAll();
        if (mall == null)
            return null;
        bdd.close();

        while (idx < mall.size())
        {
            String Title = mall.get(idx).getTitle();
/*            String finaltitle = Title.substring(0, 1).toUpperCase() + Title.substring(1);*/ // TODO faire un uppercase sur le titre
            Log.e("original date", mall.get(idx).getDate());
            if (Title.length() > 20)
                Title = Title.substring(0, 20) + "..";
            String DisplayDate = Title + " - " + getCleanDate(mall.get(idx).getDate()) + " " + mall.get(idx).getHour() ;
            Log.e("display date", DisplayDate);
            date.add(DisplayDate);
            idx++;
        }

        return date;
    }

    protected String getCleanDate(String date)
    {
        String finaldate;
        finaldate = date.split("-")[0] + " ";
        finaldate += getMonthstr(date.split("-")[1]) + " "; // TODO afficher le mois en toute lettre.. connaitre comment sont sortie les mois..
        finaldate += date.split("-")[2];
        Log.e("date finaldate", finaldate);
        return finaldate;
    }

    protected String getMonthstr(String month)
    {
        Log.e("month written in DB" , month);
        switch (month)
        {
            case "janv." :
                return ("Janvier");
            case "fév." :
                return ("Fevrier");
            case "mar." :
                return ("Mars");
            case "avr." :
                return ("Avril");
            case "mai" :
                return ("Mai");
            case "juin." :
                return ("Juin");
            case "juil." :
                return ("Juillet");
            case "aou." :
                return ("Aout");
            case "sep." :
                return ("Septembre");
            case "oct." :
                return ("Octobre");
            case "nov." :
                return ("Novembre");
            case "déc." :
                return ("Decembre");
        }
        return null;
    }

    protected int[] getDate() {
        Calendar c = Calendar.getInstance();
        int daymonth[] = new int[2];

        daymonth[0] = c.get(Calendar.DAY_OF_MONTH);
        daymonth[1]= c.get(Calendar.MONTH);
        return daymonth;
    }

    protected String getDateString(int day, int month) {
        String finalDate = new String();
        finalDate = getDay(day) + " ";
        finalDate += String.valueOf(day) + " ";
        finalDate += getMonth(month) + " 2015";
        return finalDate;
    }

    private String getMonth(int month)
    {
        switch (month)
        {
            case 1 :
                return ("Janvier");
            case 2 :
                return ("Fevrier");
            case 3 :
                return ("Mars");
            case 4 :
                return ("Avril");
            case 5 :
                return ("Mai");
            case 6 :
                return ("Juin");
            case 7 :
                return ("Juillet");
            case 8 :
                return ("Aout");
            case 9 :
                return ("Septembre");
            case 10 :
                return ("Octobre");
            case 11 :
                return ("Novembre");
            case 12 :
                return ("Decembre");
        }
        return null;
    }
    private String getDay(int day) {
        if (day > 7)
            day = day % 7;
        switch (day) {
            case 1:
                return "Lundi";
            case 2:
                return "Mardi";
            case 3:
                return "Mercredi";
            case 4:
                return "Jeudi";
            case 5:
                return "Vendredi";
            case 6:
                return "Samedi";
            case 0:
                return "Dimanche";
            default:
                return "Null";
        }
    }

}