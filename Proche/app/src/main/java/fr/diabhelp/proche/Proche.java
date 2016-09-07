package fr.diabhelp.proche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import fr.diabhelp.proche.Utils.SharedContext;

public class Proche extends AppCompatActivity{
    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String ID_USER = "id_user";
    private ViewPager viewPager;
    private SharedPreferences settings;
    private String idUser;
    fr.diabhelp.proche.PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proche);
        try {
            SharedContext.setContext(getApplicationContext().createPackageContext(
                    "fr.diabhelp.diabhelp",
                    Context.MODE_PRIVATE));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        settings = SharedContext.getSharedContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        idUser = settings.getString(ID_USER, "");
        if (idUser.equals(""))
            idUser = "26";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Suivi des proches");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Suivis"));
        tabLayout.addTab(tabLayout.newTab().setText("Demandes"));
        tabLayout.addTab(tabLayout.newTab().setText("Recherches"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new SuivisFragment());
        fragments.add(new DemandesFragment());
        fragments.add(new RechercheFragment());
        pagerAdapter = new fr.diabhelp.proche.PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition());}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ParametresActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getIdUser() {
        return idUser;
    }
}
