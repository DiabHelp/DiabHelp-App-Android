package fr.diabhelp.utilizationguide;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Arrays;

public class ManuelSubmenu extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String item;
    private String currentItem;
    private String parent;
    private MyArticleHandler articles;
    private Context context;
    private boolean children;
    private String articlesArr[];
    private String categories[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        context = getApplicationContext();
        articles = new MyArticleHandler(context);
        categories = articles.getDistinctCategories();

        if (bundle != null && bundle.getString("title")!= null){
            item = bundle.getString("title");
            currentItem = bundle.getString("currentName");
            parent = bundle.getString("parentName");
            children = bundle.getBoolean("children");

            //if the parent contains articles, then we can access it's child
            if (articles.getArticlesTitles(parent).length > 0)
                children = true;
        }
        else
            item = "Error";
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        if (!children)
            mAdapter = new MyAdapter(categories, currentItem, parent, false, articles);
        else{
            articlesArr = MyArticleHandler.getArticlesTitles(item);
            mAdapter = new MyAdapter(articlesArr, currentItem, parent, true, articles);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_faq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks
        // on the Home/Up button, so long as you specify a parent activity in AndroidManifest
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
