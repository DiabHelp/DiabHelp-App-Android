package fr.diabhelp.carnetdesuivi.Carnet;

/**
 * Created by naqued on 26/11/15.
 */
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;

public class ExpandableListAdapters extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;
    private DAO dao = null;
    private SQLiteDatabase db = null;
    private Carnetdesuivi _acti;

    public ExpandableListAdapters(Activity context, List<String> laptops,
                                  Map<String, List<String>> laptopCollections, Carnetdesuivi acti) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
        this._acti = acti;
        dao = DAO.getInstance(context);
        db = dao.open();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);
        }

        TextView gly = (TextView) convertView
                .findViewById(R.id.glytextview);
        TextView glu = (TextView) convertView
                .findViewById(R.id.glutextview);
        TextView fast = (TextView) convertView
                .findViewById(R.id.fasttextView);
        TextView slow = (TextView) convertView
                .findViewById(R.id.slowtextView);
        TextView hba1c = (TextView) convertView
                .findViewById(R.id.hba1ctextView);
        TextView notes = (TextView) convertView
                .findViewById(R.id.notestextView);

        ImageView glui = (ImageView) convertView
                .findViewById(R.id.glucideimg);

        ImageView glyi = (ImageView) convertView
                .findViewById(R.id.glycemiimg);

        ImageView fasti = (ImageView) convertView
                .findViewById(R.id.fastimg);

        ImageView slowi = (ImageView) convertView
                .findViewById(R.id.slowimg);

        ImageView hba1ci = (ImageView) convertView
                .findViewById(R.id.hba1cimg);

        RelativeLayout glyl = (RelativeLayout) convertView
                .findViewById(R.id.layoutgly);
        RelativeLayout glul = (RelativeLayout) convertView
                .findViewById(R.id.layoutglu);
        RelativeLayout fastl = (RelativeLayout) convertView
                .findViewById(R.id.layoutfast);
        RelativeLayout slowl = (RelativeLayout) convertView
                .findViewById(R.id.layoutslow);
        RelativeLayout hba1cl = (RelativeLayout) convertView
                .findViewById(R.id.layouthba1c);

        String date = laptops.get(groupPosition);
        System.out.println(date);
        String finaldate = reconstructDate(date);
        EntryOfCDS en = EntryOfCDSDAO.selectDay(finaldate, date.split("-")[1].split(" ")[4], Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""), db);

        if (en == null)
        {
            Log.e("Bug", "requete selectDay echoué");
            return convertView;
        }
        if (en.getglycemy() == 0.0 ) {
            gly.setVisibility(View.GONE);
            glyl.setVisibility(View.GONE);
            gly.setEnabled(false);
            glyi.setVisibility(View.GONE);
            glyi.setEnabled(false);
        }
        else
        {
            gly.setEnabled(true);
            gly.setVisibility(View.VISIBLE);
            glyl.setVisibility(View.VISIBLE);
            glyi.setEnabled(true);
            glyi.setVisibility(View.VISIBLE);
        }
        if (en.getGlucide() == 0.0) {
            glu.setVisibility(View.GONE);
            glul.setVisibility(View.GONE);
            glui.setVisibility(View.GONE);
            glu.setEnabled(false);
            glui.setEnabled(false);
        }
        else
        {
            glu.setEnabled(true);
            glui.setEnabled(true);
            glu.setVisibility(View.VISIBLE);
            glui.setVisibility(View.VISIBLE);
            glul.setVisibility(View.VISIBLE);
        }
        if (en.getHba1c() == 0.0) {
            hba1c.setVisibility(View.GONE);
            hba1cl.setVisibility(View.GONE);
            hba1c.setEnabled(false);
            hba1ci.setVisibility(View.GONE);
            hba1ci.setEnabled(false);

        }
        else
        {
            hba1c.setEnabled(true);
            hba1c.setVisibility(View.VISIBLE);
            hba1ci.setEnabled(true);
            hba1ci.setVisibility(View.VISIBLE);
            hba1cl.setVisibility(View.VISIBLE);
        }
        if (en.getFast_insu() == 0.0) {
            fast.setVisibility(View.GONE);
            fastl.setVisibility(View.GONE);
            fast.setEnabled(false);
            fasti.setVisibility(View.GONE);
            fasti.setEnabled(false);
        }
        else
        {
            fast.setEnabled(true);
            fast.setVisibility(View.VISIBLE);
            fasti.setEnabled(true);
            fasti.setVisibility(View.VISIBLE);
            fastl.setVisibility(View.VISIBLE);
        }
        if (en.getSlow_insu() == 0.0) {
            slow.setVisibility(View.GONE);
            slowl.setVisibility(View.GONE);
            slow.setEnabled(false);
            slowi.setVisibility(View.GONE);
            slowi.setEnabled(false);
        }
        else
        {
            slowi.setEnabled(true);
            slowi.setVisibility(View.VISIBLE);
            slowl.setVisibility(View.VISIBLE);
            slow.setEnabled(true);
            slow.setVisibility(View.VISIBLE);

        }
        if (en.getNotes() == null) {
            notes.setVisibility(View.GONE);
            notes.setEnabled(false);
        }
        else
        {
            notes.setEnabled(true);
            notes.setVisibility(View.VISIBLE);
        }
        Double glyval = en.getglycemy();

        final int sdk = android.os.Build.VERSION.SDK_INT;
        gly.setText(String.valueOf(glyval.intValue()) + "\nmg/dl");
        if (glyval >= 80 && glyval <= 120) {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                gly.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.glycemiegood));
            } else {
                gly.setBackground(context.getResources().getDrawable(R.drawable.glycemiegood));
            }
        }
        else
        {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                gly.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.glycemienogood));
            } else {
                gly.setBackground(context.getResources().getDrawable(R.drawable.glycemienogood));
            }
        }
        glu.setText(String.valueOf(en.getGlucide().intValue()) + "\n g");
        hba1c.setText(String.valueOf(en.getHba1c().doubleValue()) + "\n %");
        fast.setText(String.valueOf(en.getFast_insu())+ "\n  u");
        slow.setText(String.valueOf(en.getSlow_insu())+ "\n  u");
        notes.setText(en.getNotes());

        return convertView;
    }

    protected String reconstructDate(String date)
    {
        String datefinal;
        String datepart = date.split("-")[1];

        datefinal = getMonthstr(datepart.split(" ")[2]) + "-" + datepart.split(" ")[1] + "-" + datepart.split(" ")[3];

        return datefinal;
    }
    protected String getMonthstr(String month) //TODO utiliser la classe DateMagnifier
    {
        switch (month)
        {
            case "Janvier" :
                return ("01");
            case "Fevrier" :
                return ("02");
            case "Mars" :
                return ("03");
            case "Avril" :
                return ("04");
            case "Mai" :
                return ("05");
            case "Juin" :
                return ("06");
            case "Juillet" :
                return ("07");
            case "Aout" :
                return ("08");
            case "Septembre" :
                return ("09");
            case "Octobre" :
                return ("10");
            case "Novembre" :
                return ("11");
            case "Decembre" :
                return ("12");
        }
        return null;
    }

    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public int getGroupCount() {
        return laptops.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        final int grppos = groupPosition;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        ImageButton infoDay = (ImageButton) convertView.findViewById(R.id.infoday);
        infoDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Carnetdesuivi)_acti, DayResultActivity.class);

                String _title = (String) getGroup(grppos);
                String datesplit = reconstructDate(_title);
                String Hour = _title.split("-")[1].split(" ")[4];

                intent.putExtra("date", datesplit);
                intent.putExtra("hour", Hour);
                _acti.startActivity(intent);
                _acti.finish();
            }
        });
        infoDay.setFocusable(false);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}