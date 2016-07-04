package fr.diabhelp.diabhelp.UtilizationGuide;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import fr.diabhelp.diabhelp.R;


public class ArticleAdapter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get ScreenSize
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Context context = getApplicationContext();
        MyArticleHandler articles = MyArticleHandler.getInstance(context);
        Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.display_content);
        ScrollView MainView = (ScrollView) findViewById(R.id.contentScrollView);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        String item;
        if (bundle != null && (item = bundle.getString("title")) != null){
            String category = bundle.getString("parentName");
            Article art = MyArticleHandler.getArticle(item, category);

            if (art != null){
                tb.setTitle(art.title);
                int items = art.content.length;
                TextView content;
                ImageView image;
                String finalText;
                for  (int a = 0; a < items; a++){
                    image = (ImageView) getLayoutInflater().inflate(R.layout.art_image_view, null);

                    if (art.content[a].startsWith("[Image]")){
                        String[] fullTag = art.content[a].split("]");

                        if (fullTag.length > 1){
                            String imgName = fullTag[1];
                            int imgId;
                            imgId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                            Drawable drawable = null;
                            if (imgId != 0)
                                drawable = ContextCompat.getDrawable(context, imgId);
                            if (drawable != null) {
                                image.setImageDrawable(drawable);
                                ll.addView(image);
                            }
                            else{
                                content = (TextView) getLayoutInflater().inflate(R.layout.art_err, null);
                                content.setText("Image [" + imgName + "] not found");
                                ll.addView(content);
                            }
                        }
                    }
                    else if (art.content[a].startsWith("[Space]")) {
                        Space sp = new Space(this);
                        sp.setMinimumHeight(30);
                        ll.addView(sp);
                    }
                    else {
                        if (art.content[a].startsWith("[Title]")) {
                            content = (TextView) getLayoutInflater().inflate(R.layout.art_title_tv, null);
                            finalText = art.content[a].split("]")[1];
                        }
                        else {
                            content = (TextView) getLayoutInflater().inflate(R.layout.art_paragraph_tv, null);
                            finalText = art.content[a];
                        }
                        content.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                        );
                        content.setText(finalText);
                        ll.addView(content);
                    }
                }
                MainView.addView(ll);
            }
        }
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks
        // on the Home/Up button, so long as you specify a parent activity in AndroidManifest
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }*/
}