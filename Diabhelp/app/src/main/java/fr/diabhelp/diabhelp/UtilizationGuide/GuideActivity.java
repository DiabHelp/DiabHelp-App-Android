package fr.diabhelp.diabhelp.UtilizationGuide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.diabhelp.diabhelp.R;

public class GuideActivity extends AppCompatActivity {
    private String mDataSet[] = null;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_manual);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            MyArticleHandler art = new MyArticleHandler(this);
            mDataSet = art.getDistinctCategories();

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            RecyclerView.Adapter mAdapter = new MyAdapter(mDataSet, null, null, false,
                    MyArticleHandler.getInstance(getApplicationContext()));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}