package fr.diabhelp.faq;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class FaqSubmenu extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String item;
    private String diabete[] = {"Comprendre le Diabete",
            "Qu'elles sont les symptomes ?", "Hypo/Hyper Glycémie, qu'est-ce que c'est ?",
            "Hypoglycémie, qu'elles sont les risques ?", "Hyperglycémie, qu'elles sont les risques ?",
            "Les différents type de Diabète", "L'Acétone - HBA1C - Basal - termes..", "Traitement diabète sans insuline ?",
            "Diabete = interdiction ?", "Chose à éviter"
            };

    private String traitements[] = {"Piqures",
            "Cachet", "Pompes à insuline",
            "Patch", "Lentille google",
    };

    private String typediabete[] = {"Diabète de Type 1",
            "Diabète de Type 2", "Diabète gestationelle"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("title")!= null)
        {
            item = bundle.getString("title");
        }
        else
            item = "Error";

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        if (item.compareTo("Type de Diabète, Cause, Traitements") == 0)
            mAdapter = new MyAdapter(typediabete);
        else
            Log.e("error", "no adapter found..");
        mRecyclerView.setAdapter(mAdapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
