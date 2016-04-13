package fr.diabhelp.utilizationguide;

/**
 * Created by Maxime on 10/03/2016.
 */


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        private RecyclerView.Adapter mAdapter;
        private MyArticleHandler articleHandler;
        private String mDataSet[] = articleHandler.getCategories();
                //{"Premiers pas dans l'application", "Gestion des modules - Le catalogue",
                //"Connexion, inscription", "Les différents modules disponibles", "Divers"};

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            articleHandler = new MyArticleHandler(getApplicationContext());
            setContentView(R.layout.activity_manual);
  //          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //          setSupportActionBar(toolbar);

            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new MyAdapter(mDataSet, null, null, false, articleHandler);
            mRecyclerView.setAdapter(mAdapter);
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