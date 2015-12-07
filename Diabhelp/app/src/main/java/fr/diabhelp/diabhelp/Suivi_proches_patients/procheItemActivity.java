package fr.diabhelp.diabhelp.Suivi_proches_patients;

import android.content.Intent;
import android.media.Image;
import android.os.Parcel;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.List;

import fr.diabhelp.diabhelp.R;

public class procheItemActivity extends AppCompatActivity {

    private Toolbar procheToolbar;
    private Proche proche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proche_item);
        getExtras();
        initToolbar(proche.getNickName());
        ((ImageView)findViewById(R.id.proche_avatar)).setImageResource(R.drawable.ic_proche);
    }

    public void getExtras()
    {
        Bundle extra = getIntent().getExtras();
        proche = extra.getParcelable(ListeProches.PROCHE);
    }

    public void initToolbar(String nickname)
    {
        procheToolbar = (Toolbar) findViewById(R.id.proche_toolbar);
        procheToolbar.setTitle(nickname);
        setSupportActionBar(procheToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish();
            return true;
        }
        return false;
    }

}
