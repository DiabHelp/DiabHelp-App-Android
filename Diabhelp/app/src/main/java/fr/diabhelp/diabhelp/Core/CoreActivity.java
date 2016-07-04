package fr.diabhelp.diabhelp.Core;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.FAQ.Faq;
import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.R;


public class CoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<PInfo> app;
    private ArrayList<String> listApp;
    private String[] web;
    private Drawable[] img;
    private DAO bdd;
    private Boolean NetState;
    private TabLayout tabLayout;
    public static SharedPreferences _settings = null;

    class PInfo{
        public String appname = "";
        public String pname = "";
        public String versionName = "";
        public int versionCode = 0;
        public Drawable icon;
        public String publicSourceDir;

        private void DebugPrint() {
            Log.v(appname, appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
        }
    }

    public ArrayList<PInfo> getAppList() { return this.app; }
    public String[] getAppStringList() { return this.web; }
    public Drawable[] getAppDrawableList() { return this.img; }

    public void updateModuleList()
    {
        app = getPackages();
    }


    public Boolean getNetState() {return this.NetState; }

    public void setNetStateAndChange(Boolean netstate) { this.NetState = netstate;
    // Change layout
        // apply Drawable.mutate().setColorFilter( 0xffff0000, Mode.MULTIPLY) on offline icone
        if (this.NetState == true)
            Toast.makeText(this, "Internet on", Toast.LENGTH_LONG).show();
        else if (this.NetState == false)
            Toast.makeText(this, "Internet off", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        _settings = getSharedPreferences(ConnexionActivity.PREF_FILE, MODE_WORLD_READABLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listApp = new ArrayList<String>();
        app = getPackages();

        //init menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Mes Outils"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Catalogue"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("Paramètre"), 2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<Fragment> pages = new ArrayList<>(3);
        pages.add(new AccueilFragment());
        pages.add(new CatalogueFragment());
        pages.add(new ParametresFragment());
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), pages);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            Intent mainIntent = new Intent(CoreActivity.this, ProfileActivity.class);
            CoreActivity.this.startActivity(mainIntent);
        } else if (id == R.id.nav_website) {
            String url = "http://www.diabhelp.fr";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_faq) {
            Intent mainIntent = new Intent(CoreActivity.this, Faq.class);
            CoreActivity.this.startActivity(mainIntent);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_facebook) {
            String url = "https://www.facebook.com/diabhelp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        else if (id == R.id.nav_twitter) {
            // TODO Twitter account
            String url = "https://www.facebook.com/diabhelp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_mod:
/*                tabLayout.setta*/
/*                launch_parametre();*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*    private void launch_parametre()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HelloFragment hello = new HelloFragment();
        fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
        fragmentTransaction.commit();
    }*/
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


    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        int ctr = 0;

        PInfo newInfo;

        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        int nbDApp = count_dApp(packs);
        img = new Drawable[nbDApp];
        web = new String[nbDApp];
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
                newInfo.publicSourceDir = p.applicationInfo.publicSourceDir;
                newInfo.icon = p.applicationInfo.loadIcon(getPackageManager()); // choper le drawable
                if (newInfo.icon != null && newInfo.appname != null) {
                    img[ctr] = newInfo.icon;
                    web[ctr++] = newInfo.appname;
                }
                res.add(newInfo);
            }
        }
        return res;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = ConnexionActivity._settings.edit();
        edit.putString(ConnexionActivity.TOKEN, "");
        edit.putString(ConnexionActivity.TYPE_USER, "");
        edit.putString(ConnexionActivity.ID_USER, "");
        edit.commit();
    }
}