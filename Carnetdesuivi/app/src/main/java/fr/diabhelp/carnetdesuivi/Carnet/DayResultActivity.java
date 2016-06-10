package fr.diabhelp.carnetdesuivi.Carnet;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.DataBase.DAO;
import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.R;
import fr.diabhelp.carnetdesuivi.Utils.DateMagnifier;

public class DayResultActivity extends AppCompatActivity {

    final int sdk = android.os.Build.VERSION.SDK_INT;

    public enum TXTedit {
        DAYTEXT(0),
        PLACETEXT(1),
        SPORTDESC(2),
        SPORTDETAIL(3),
        GLYCEMIE(4),
        GLUCIDE(5),
        FASTINSU(6),
        SLOWINSU(7),
        HBA1C(8),
        HSPORT2(9),
        NOTES(10),
        TITLE(11);

        private final int value;

        private TXTedit(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    ;

    public enum Linear {
        BREAKFAST(0),
        LAUNCH(1),
        DINER(2),
        ENCAS(3),
        SLEEP(4),
        WAKEUP(5),
        NIGHT(6),
        WORKOUT(7),
        HYPO(8),
        HYPER(9),
        WORK(10),
        HOME(11),
        ALCOHOL(12),
        PERIOD(13),
        SPORT2(14),
        NOTES(15),
        PLACE(16),
        ACTIVITYW(17),
        ACTIVITYTY(18);

        private final int value;

        private Linear(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    ;

    public enum Relative {
        GLY(0),
        GLU(1),
        FAST(2),
        SLOW(3),
        HBA1C(4);


        private final int value;

        private Relative(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    ;

    ArrayList<RelativeLayout> _relativeCell;
    ArrayList<LinearLayout> _linearCell;
    ArrayList<TextView> _txtCell;

    String _date;
    String _hour;
    DAO _bdd;
    EntryOfCDS inf;
    DateMagnifier _mt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultday);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Entrée détaillé");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Carnetintent = new Intent(DayResultActivity.this, Carnetdesuivi.class);
                startActivity(Carnetintent);
            }
        });
        _relativeCell = new ArrayList<RelativeLayout>();
        _linearCell = new ArrayList<LinearLayout>();
        _txtCell = new ArrayList<TextView>();
        _mt = new DateMagnifier();
        _bdd = new DAO(this);

        Intent intent = getIntent();
        _date = intent.getStringExtra("date");
        _hour = intent.getStringExtra("hour");
        Log.e("gethour oncreate", _hour);
        initArray();
        initView();

    }


    protected void initView() {

        _bdd.open();
        inf = _bdd.SelectDay(_date, _hour);
        _txtCell.get(TXTedit.DAYTEXT.getValue()).setText(_mt.getCleanDate(_date) + " " + _hour);
        _txtCell.get(TXTedit.TITLE.getValue()).setText(inf.getTitle());
        if (inf.getGlucide() == null || inf.getglycemy() == 0.0) {
            _relativeCell.get(Relative.GLU.getValue()).setVisibility(View.GONE);
        } else {
            ImageView glyi = (ImageView) findViewById(R.id.glycemiimg);

            TextView txtgly2 = (TextView) findViewById(R.id.txtglucide);
            txtgly2.setVisibility(View.VISIBLE);

            Double gluglu = inf.getglycemy();
            _relativeCell.get(Relative.GLY.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.GLYCEMIE.getValue()).setText(String.valueOf(gluglu.intValue() + "\nmg/dl"));
            if (gluglu >= 80 && gluglu <= 120) {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    glyi.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.glycemiegood));
                } else {
                    glyi.setBackground(this.getResources().getDrawable(R.drawable.glycemiegood));
                }
            } else {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    glyi.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.glycemienogood));
                } else {
                    glyi.setBackground(this.getResources().getDrawable(R.drawable.glycemienogood));
                }
            }
        }

        int count = 0;

        if (inf.getGlucide() == null || inf.getGlucide() == 0)
            _relativeCell.get(Relative.GLU.getValue()).setVisibility(View.GONE);
        else {
            _relativeCell.get(Relative.GLU.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.GLUCIDE.getValue()).setText(String.valueOf(inf.getGlucide()) + "\ng");
        }
        if (inf.getFast_insu() == null || inf.getFast_insu() == 0)
            _relativeCell.get(Relative.FAST.getValue()).setVisibility(View.GONE);
        else {
            _relativeCell.get(Relative.FAST.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.FASTINSU.getValue()).setText(String.valueOf(inf.getFast_insu()) + "\n  u");
        }
        if (inf.getSlow_insu() == null || inf.getSlow_insu() == 0)
            _relativeCell.get(Relative.SLOW.getValue()).setVisibility(View.GONE);
        else {
            _relativeCell.get(Relative.SLOW.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.SLOWINSU.getValue()).setText(String.valueOf(inf.getSlow_insu()) + "\n  u");
        }
        if (inf.getHba1c() == null || inf.getHba1c() == 0)
            _relativeCell.get(Relative.HBA1C.getValue()).setVisibility(View.GONE);
        else {
            _relativeCell.get(Relative.HBA1C.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.HBA1C.getValue()).setText(String.valueOf(inf.getHba1c()));
        }
        if (inf.getBreakfast() == null || inf.getBreakfast() == 0)
            _linearCell.get(Linear.BREAKFAST.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(count++).setVisibility(View.VISIBLE);

        if (inf.getLaunch() == null || inf.getLaunch() == 0)
            _linearCell.get(Linear.LAUNCH.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.LAUNCH.getValue()).setVisibility(View.VISIBLE);

        if (inf.getDiner() == null || inf.getDiner() == 0)
            _linearCell.get(Linear.DINER.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.DINER.getValue()).setVisibility(View.VISIBLE);

        if (inf.getEncas() == null || inf.getEncas() == 0)
            _linearCell.get(Linear.ENCAS.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.ENCAS.getValue()).setVisibility(View.VISIBLE);

        if (inf.getSleep() == null || inf.getSleep() == 0)
            _linearCell.get(Linear.SLEEP.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.SLEEP.getValue()).setVisibility(View.VISIBLE);

        if (inf.getWakeup() == null || inf.getWakeup() == 0) {
            _linearCell.get(Linear.WAKEUP.getValue()).setVisibility(View.GONE);
        } else {
            _linearCell.get(Linear.WAKEUP.getValue()).setVisibility(View.VISIBLE);
        }

        if (inf.getNight() == null || inf.getNight() == 0)
            _linearCell.get(Linear.NIGHT.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.NIGHT.getValue()).setVisibility(View.VISIBLE);


        if (inf.getWorkout() == null || inf.getWorkout() == 0) // detail activity
        {
            _linearCell.get(Linear.WORKOUT.getValue()).setVisibility(View.GONE);

        } else
            _linearCell.get(Linear.WORKOUT.getValue()).setVisibility(View.VISIBLE);

        if (inf.getHypogly() == null || inf.getHypogly() == 0)
            _linearCell.get(Linear.HYPO.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.HYPO.getValue()).setVisibility(View.VISIBLE);

        if (inf.getHypergly() == null || inf.getHypergly() == 0)
            _linearCell.get(Linear.HYPER.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.HYPER.getValue()).setVisibility(View.VISIBLE);

        if (inf.getAtwork() == null || inf.getAtwork() == 0)
            _linearCell.get(Linear.WORK.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.WORK.getValue()).setVisibility(View.VISIBLE);


        if (inf.getAthome() == null || inf.getAthome() == 0)
            _linearCell.get(Linear.HOME.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.HOME.getValue()).setVisibility(View.VISIBLE);

        if (inf.getAlcohol() == null || inf.getAlcohol() == 0)
            _linearCell.get(Linear.ALCOHOL.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.ALCOHOL.getValue()).setVisibility(View.VISIBLE);

        if (inf.getPeriod() == null || inf.getPeriod() == 0)
            _linearCell.get(Linear.PERIOD.getValue()).setVisibility(View.GONE);
        else
            _linearCell.get(Linear.PERIOD.getValue()).setVisibility(View.VISIBLE);

        if (inf.getActivity() == null || inf.getActivity() == "" || inf.getActivity().length() == 0) {
            _linearCell.get(Linear.SPORT2.getValue()).setVisibility(View.GONE);
            _txtCell.get(TXTedit.SPORTDESC.getValue()).setVisibility(View.GONE);
            _txtCell.get(TXTedit.SPORTDETAIL.getValue()).setVisibility(View.GONE);
            _txtCell.get(TXTedit.HSPORT2.getValue()).setVisibility(View.GONE);
        } else {
            _linearCell.get(Linear.SPORT2.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.HSPORT2.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.HSPORT2.getValue()).setText(inf.getActivity());
        }

        if (inf.getActivityType() == null || inf.getActivityType() == "" || inf.getActivityType().length() == 0) {
            _txtCell.get(TXTedit.SPORTDESC.getValue()).setVisibility(View.GONE);
            _txtCell.get(TXTedit.SPORTDETAIL.getValue()).setVisibility(View.GONE);

        } else {
            _linearCell.get(Linear.SPORT2.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.SPORTDESC.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.SPORTDETAIL.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.SPORTDETAIL.getValue()).setText(inf.getActivityType());
        }

        if (inf.getNotes() == null || inf.getNotes() == "" || inf.getNotes().length() == 0) {
            _linearCell.get(Linear.NOTES.getValue()).setVisibility(View.GONE);

        }
        else {
            _linearCell.get(Linear.NOTES.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.NOTES.getValue()).setText(inf.getNotes());
        }

        Log.e("getplace for icone", "yo:" + inf.getPlace() + "$");
        if (inf.getPlace() == null || inf.getPlace() == "") {
            _linearCell.get(Linear.PLACE.getValue()).setVisibility(View.GONE);
            ImageView img = (ImageView) findViewById(R.id.ic_placeentry);
            img.setVisibility(View.GONE);
            Log.e("test place", "yohou");
        }
        else {
            _linearCell.get(Linear.PLACE.getValue()).setVisibility(View.VISIBLE);
            _txtCell.get(TXTedit.PLACETEXT.getValue()).setText(inf.getPlace());
            ImageView img = (ImageView) findViewById(R.id.ic_placeentry);
//            img.setVisibility(View.VISIBLE);
            Log.e("test place", "yaha");
        }
        _bdd.close();
    }

    protected void initArray() {
        _txtCell.add(TXTedit.DAYTEXT.getValue(), (TextView) findViewById(R.id.datetext));
        _txtCell.add(TXTedit.PLACETEXT.getValue(), (TextView) findViewById(R.id.textplace));
        _txtCell.add(TXTedit.SPORTDESC.getValue(), (TextView) findViewById(R.id.txtsportdesc));
        _txtCell.add(TXTedit.SPORTDETAIL.getValue(), (TextView) findViewById(R.id.txtsport2));
        _txtCell.add(TXTedit.GLYCEMIE.getValue(), (TextView) findViewById(R.id.glytextview));
        _txtCell.add(TXTedit.GLUCIDE.getValue(), (TextView) findViewById(R.id.glutextview));
        _txtCell.add(TXTedit.FASTINSU.getValue(), (TextView) findViewById(R.id.fasttextView));
        _txtCell.add(TXTedit.SLOWINSU.getValue(), (TextView) findViewById(R.id.slowtextView));
        _txtCell.add(TXTedit.HBA1C.getValue(), (TextView) findViewById(R.id.hba1ctextView));
        _txtCell.add(TXTedit.HSPORT2.getValue(), (TextView) findViewById(R.id.ndheuresport));
        _txtCell.add(TXTedit.NOTES.getValue(), (TextView) findViewById(R.id.detailfull));
        _txtCell.add(TXTedit.TITLE.getValue(), (TextView) findViewById(R.id.textname));


        _relativeCell.add(Relative.GLY.getValue(), (RelativeLayout) findViewById(R.id.layoutgly));
        _relativeCell.add(Relative.GLU.getValue(), (RelativeLayout) findViewById(R.id.layoutglu));
        _relativeCell.add(Relative.FAST.getValue(), (RelativeLayout) findViewById(R.id.layoutfast));
        _relativeCell.add(Relative.SLOW.getValue(), (RelativeLayout) findViewById(R.id.layoutslow));
        _relativeCell.add(Relative.HBA1C.getValue(), (RelativeLayout) findViewById(R.id.layouthba1c));

        _linearCell.add(Linear.BREAKFAST.getValue(), (LinearLayout) findViewById(R.id.layout_breakfast));
        _linearCell.add(Linear.LAUNCH.getValue(), (LinearLayout) findViewById(R.id.launch));
        _linearCell.add(Linear.DINER.getValue(), (LinearLayout) findViewById(R.id.diner));
        _linearCell.add(Linear.ENCAS.getValue(), (LinearLayout) findViewById(R.id.layout_encas));
        _linearCell.add(Linear.SLEEP.getValue(), (LinearLayout) findViewById(R.id.layout_sleep));
        _linearCell.add(Linear.WAKEUP.getValue(), (LinearLayout) findViewById(R.id.layout_wakeup));
        _linearCell.add(Linear.NIGHT.getValue(), (LinearLayout) findViewById(R.id.layout_night));
        _linearCell.add(Linear.WORKOUT.getValue(), (LinearLayout) findViewById(R.id.layout_sport));
        _linearCell.add(Linear.HYPO.getValue(), (LinearLayout) findViewById(R.id.layout_hypo));
        _linearCell.add(Linear.HYPER.getValue(), (LinearLayout) findViewById(R.id.layout_hyper));
        _linearCell.add(Linear.WORK.getValue(), (LinearLayout) findViewById(R.id.layout_work));
        _linearCell.add(Linear.HOME.getValue(), (LinearLayout) findViewById(R.id.layout_home));
        _linearCell.add(Linear.ALCOHOL.getValue(), (LinearLayout) findViewById(R.id.layout_alcohol));
        _linearCell.add(Linear.PERIOD.getValue(), (LinearLayout) findViewById(R.id.layout_period));
        _linearCell.add(Linear.SPORT2.getValue(), (LinearLayout) findViewById(R.id.layout_sport2));
        _linearCell.add(Linear.NOTES.getValue(), (LinearLayout) findViewById(R.id.layoutdetail));
        _linearCell.add(Linear.PLACE.getValue(), (LinearLayout) findViewById(R.id.layoutplace));
    }

    @Override
    public void onBackPressed() {
        Intent Dayintent = new Intent(DayResultActivity.this, Carnetdesuivi.class);
        DayResultActivity.this.startActivity(Dayintent);
        this.finish();
    }

    public void updateEntry(View v) {
        Intent intent = new Intent(DayResultActivity.this, EntryActivity.class);

        intent.putExtra("title", inf.getTitle());
        intent.putExtra("place", inf.getPlace());
        intent.putExtra("glucide", inf.getGlucide());
        intent.putExtra("activity", inf.getActivity());
        intent.putExtra("activityType", inf.getActivityType());
        intent.putExtra("notes", inf.getNotes());
        intent.putExtra("date", inf.getDate());
        intent.putExtra("fast_insu", inf.getFast_insu());
        intent.putExtra("slow_insu", inf.getSlow_insu());
        intent.putExtra("hba1c", inf.getHba1c());
        intent.putExtra("hour", inf.getHour());
        intent.putExtra("date", inf.getDate());

        intent.putExtra("glycemy", inf.getglycemy());

        intent.putExtra("launch", inf.getLaunch());
        intent.putExtra("diner", inf.getDiner());
        intent.putExtra("encas", inf.getEncas());
        intent.putExtra("sleep", inf.getSleep());
        intent.putExtra("wakeup", inf.getWakeup());
        intent.putExtra("night", inf.getNight());
        intent.putExtra("workout", inf.getWorkout());
        intent.putExtra("hypogly", inf.getHypogly());
        intent.putExtra("hypergly", inf.getHypergly());
        intent.putExtra("atwork", inf.getAtwork());
        intent.putExtra("athome", inf.getAthome());
        intent.putExtra("alcohol", inf.getAlcohol());
        intent.putExtra("period", inf.getPeriod());
        intent.putExtra("breakfast", inf.getBreakfast());


        DayResultActivity.this.startActivity(intent);
        DayResultActivity.this.finish();
    }

    public void DeleteEntry(View v)
    {

        new AlertDialog.Builder(this)
                .setTitle("Suppresion")
                .setMessage("Vous êtes sur le point de supprimer cette entrée. Etes vous sur de vouloir continuer ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        _bdd.open();
                        _bdd.deleteDay(inf.getDate(), inf.getHour());
                        _bdd.close();
                        Intent intent = new Intent(DayResultActivity.this, Carnetdesuivi.class);

                        DayResultActivity.this.startActivity(intent);
                        DayResultActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
