package fr.diabhelp.diabhelp.UtilizationGuide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import fr.diabhelp.diabhelp.R;

public class ManuelSubmenu extends AppCompatActivity {
    private String item;
    private String currentItem;
    private String parent;
    private boolean children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        //Create / get the articleHandle instance if not already set
        MyArticleHandler.getInstance(getApplicationContext());

        if (bundle != null && bundle.getString("title")!= null){
            item = bundle.getString("title");
            toolbar.setTitle(item);
            parent = bundle.getString("parentName");
            children = bundle.getBoolean("children");
            currentItem = bundle.getString("currentName");
            //if the parent contains articles, then we can access it's child
            if (MyArticleHandler.getArticlesTitles(parent).length > 0)
                children = true;
        }
        else
            toolbar.setTitle(R.string.app_name);

        String[] categories = MyArticleHandler.getDistinctCategories();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter;
        if (!children)
            mAdapter = new MyAdapter(categories, currentItem, parent, false,
                    MyArticleHandler.getInstance(getApplicationContext()));
        else{
            String[] articlesArr = MyArticleHandler.getArticlesTitles(item);
            mAdapter = new MyAdapter(articlesArr, currentItem, parent, true,
                    MyArticleHandler.getInstance(getApplicationContext()));
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_faq, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks
        // on the Home/Up button, so long as you specify a parent activity in AndroidManifest
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
