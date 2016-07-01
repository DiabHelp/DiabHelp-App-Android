package fr.diabhelp.diabhelp.FAQ;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.diabhelp.diabhelp.R;

public class FaqSubmenu extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String item;
    private String diabete[] = {"Comprendre le diabete",
            "Qu'elles sont les symptômes ?", "Hypo/hyper glycémie, qu'est-ce que c'est ?",
            "Hypoglycémie, qu'elles sont les risques ?", "Hyperglycémie, qu'elles sont les risques ?",
            "Les différents type de diabète", "L'acétone - HBA1C - Basal - termes..", "Traitement diabète sans insuline ?",
            "Diabete = interdiction ?", "Choses à éviter"
            };

    private String traitements[] = {"Piqures",
            "Cachet", "Pompes à insuline",
            "Patch", "Lentille google",
    };

    private String typediabete[] = {"Diabète de Type 1",
            "Diabète de Type 2", "Diabète gestationel"
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


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        if (item.compareTo("Types de diabètes, cause, traitements") == 0)
            mAdapter = new MyAdapter(typediabete);
        else
            Log.e("error", "no adapter found..");
        mRecyclerView.setAdapter(mAdapter);




    }
}
