package fr.diabhelp.diabhelp.Core;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.R;


public class CoreActivity extends AppCompatActivity {

    private ArrayList<PInfo> app;
    private ArrayList<String> listApp;
    private String[] web;
    private Drawable[] img;
    private DAO bdd;

    class PInfo{
        public String appname = "";
        public String pname = "";
        public String versionName = "";
        public int versionCode = 0;
        public Drawable icon;

        private void DebugPrint() {
            Log.v(appname, appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
        }
    }

    public ArrayList<PInfo> getAppList() { return this.app; }
    public String[] getAppStringList() { return this.web; }
    public Drawable[] getAppDrawableList() { return this.img; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listApp = new ArrayList<String>();
        app = getPackages();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Accueil"));
        tabLayout.addTab(tabLayout.newTab().setText("Catalogue"));
        tabLayout.addTab(tabLayout.newTab().setText("Paramètre"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_user)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).DebugPrint();

        }
        return apps;
    }

    private int count_dApp(List<PackageInfo> packs) {
        int ctr = 0;
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp"))
                ctr++;
        }
        return ctr;
    }

    private PInfo nav_manageur(Drawable t)
    {
        PInfo inf = new PInfo();
        inf.appname = "Site Diabhelp";
        inf.icon = t;
        inf.versionCode = 0;
        inf.pname = "diab_website";
        return inf;
    }
    private PInfo help_manageur(Drawable t)
    {
        PInfo inf = new PInfo();
        inf.appname = "Aide";
        inf.icon = t;
        inf.versionCode = 0;
        inf.pname = "help_me";
        return inf;
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        int ctr = 0;

        PInfo newInfo;

        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        int nbDApp = count_dApp(packs);
        img = new Drawable[nbDApp + 4];
        web = new String[nbDApp + 4];
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp")) {

                if ((!getSysPackages) && (p.versionName == null)) {
                    continue;
                }
                newInfo = new PInfo();
                newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
                newInfo.pname = p.packageName;
                listApp.add(p.packageName);
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                newInfo.icon = p.applicationInfo.loadIcon(getPackageManager()); // choper le drawable
                if (newInfo.icon != null && newInfo.appname != null) {
                    img[ctr] = newInfo.icon;
                    web[ctr++] = newInfo.appname;
                }
                res.add(newInfo);
            }
        }
        //set du nav
        newInfo = nav_manageur(getResources().getDrawable(R.drawable.web)); // icone par défaut a changer !!
        img[ctr] = newInfo.icon;
        web[ctr++] = newInfo.appname;
        res.add(newInfo);
        // set de l'help
        newInfo = help_manageur(getResources().getDrawable(R.drawable.help)); // icone par défaut a changer !!
        img[ctr] = newInfo.icon;
        web[ctr] = newInfo.appname;
        res.add(newInfo);
        return res;
    }
}