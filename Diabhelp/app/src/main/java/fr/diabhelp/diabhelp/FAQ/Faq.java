package fr.diabhelp.diabhelp.FAQ;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.R;

public class Faq extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String myDataset[] = {"Savoir réagir face à un malaise", "Types de diabètes, cause, traitements",
            "Diabète égal interdiction ?\nSavoir gérer son repas",
            "Trucs et astuces pour ne pas se faire surprendre", "Cigarette/Alcool/Autres.. Que se passe-t-il ?",
            "Aide financiere liée au diabète",
            "Sexualité / Grossesse / Contraception", "Apprivoiser la maladie"};

//TODO ; Equipement lié au Diabete ; Etude et vie professionelle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);

        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onBackPressed() {
    Intent mainIntent = new Intent(Faq.this, CoreActivity.class);
    Faq.this.startActivity(mainIntent);
    Faq.this.finish();
    }
}
