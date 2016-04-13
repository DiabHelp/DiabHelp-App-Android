package fr.diabhelp.utilizationguide;

/**
 * Created by Maxime on 10/03/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;


public class ArticleAdapter extends AppCompatActivity {
    private static MyArticleHandler articles;
    private String item;
    private String category;
    private Context context;
    private static Article art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        articles = new MyArticleHandler(context);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && (item = bundle.getString("title")) != null){
            category = bundle.getString("parentName");
            art = articles.getArticle(item, category);
//            Toast tst = Toast.makeText(context, "looking for article : " + item + " in category " + category, Toast.LENGTH_SHORT);
//            tst.show();

/*            if (art != null){
                Toast tst2 = Toast.makeText(context, "article found : " + art.getTitle(), Toast.LENGTH_SHORT);
                tst2.show();
            }*/

            setContentView(R.layout.cardview_activity);
            TextView title = (TextView) findViewById(R.id.article_title);
            TextView content = (TextView) findViewById(R.id.article_content);
            android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            if (art != null){
//                title.setText(art.title);
                content.setText(art.content);
                content.setTextSize(15);
                tb.setTitle(art.title);
            }
        }

        //          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //          setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks
        // on the Home/Up button, so long as you specify a parent activity in AndroidManifest
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
}