package fr.diabhelp.suiviprochepatient;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listProchesView;
    private RecyclerView.LayoutManager recycleViewManager;
    private RecyclerView.Adapter listAdapter;
    private List<Proche> proches;
    private Toolbar procheToolbar;
    private Proche proche;
    public static final String PROCHE = "fr.diabhelp.diabhelp.Suivi_proches_patients.proche";
    public int currentApiVersion = Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        listProchesView = (RecyclerView) findViewById(R.id.proches_recycler_view);
        listProchesView.setHasFixedSize(true);
        recycleViewManager = new LinearLayoutManager(this);
        listProchesView.setLayoutManager(recycleViewManager);

        proches = new ArrayList<>();
        proches.add(new Proche(1, "Sumbers"));
        proches.add(new Proche(2, "Lillule"));
        proches.add(new Proche(3, "Melanie"));
        proches.add(new Proche(0, getString(R.string.ajouter_un_proche)));

        listAdapter = new SuiviProche_listAdapter(proches, this);
        listProchesView.setAdapter(listAdapter);
//        for (int i = 0; i < listProchesView.getChildCount(); i++)
//        {
//
//            (listProchesView.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v){
//                    System.out.println("position cliquée = " + listProchesView.getChildAdapterPosition(v));
//                }
//            });
//        }
    }


    public void initToolbar()
    {
        procheToolbar = (Toolbar) findViewById(R.id.liste_proches_toolbar);
        setSupportActionBar(procheToolbar);
        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void manageActionOnClick(List<Proche> listProches, Integer position, ImageView avatar) {
        if (position == listProches.size() - 1) {
            AlertDialog.Builder aD = new AlertDialog.Builder(this);
            aD.setIcon(R.drawable.ic_plus_proche);
            aD.setTitle(R.string.dialog_addproche_title);
            aD.setMessage(R.string.dialog_addproche_message);
            aD.setView(getLayoutInflater().inflate(R.layout.dialog_addproche, null));
            aD.setPositiveButton(R.string.dialog_addproche_senddemande, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            aD.show();
        }
        else {
            launchProcheSimpleView(listProches.get(position), avatar);
        }
    }

    private void launchProcheSimpleView(Proche proche, ImageView avatar) {
        Intent simpleProcheIntent = new Intent(this, procheItemActivity.class);
        if (currentApiVersion > Build.VERSION_CODES.LOLLIPOP){
            transitionForNewAPI(proche, avatar);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROCHE, proche);
        simpleProcheIntent.putExtras(bundle);
        startActivity(simpleProcheIntent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void transitionForNewAPI(Proche proche, ImageView avatar)
    {
        Intent simpleProcheIntent = new Intent(this, procheItemActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PROCHE, proche);
        simpleProcheIntent.putExtras(bundle);

        ActivityOptions ao = ActivityOptions.makeSceneTransitionAnimation(this, avatar, "proche_avatar");
        startActivity(simpleProcheIntent);
        ao.toBundle();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
