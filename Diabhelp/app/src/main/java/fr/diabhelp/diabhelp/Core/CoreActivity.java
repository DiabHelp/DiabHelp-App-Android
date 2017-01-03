package fr.diabhelp.diabhelp.Core;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.FAQ.Faq;
import fr.diabhelp.diabhelp.Menu.ParametersActivity;
import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.Models.ModuleList;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Services.RegistrationIntentService;
import fr.diabhelp.diabhelp.UtilizationGuide.GuideActivity;


public class CoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ModuleList moduleList;
    private ArrayList<ModuleList.PInfo> app;
    private Boolean NetState;
    private TabLayout tabLayout;
    public static SharedPreferences _settings;



    public Boolean getNetState() {return this.NetState; }

    public void setNetStateAndChange(Boolean netstate) {
        this.NetState = netstate;
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
        moduleList = new ModuleList(this);
        _settings = getSharedPreferences(ConnexionActivity.PREF_FILE, Context.MODE_WORLD_READABLE);
        System.out.println("core ID_USER = " + _settings.getString(ConnexionActivity.ID_USER, ""));
        System.out.println("core ROLE = " + _settings.getString(ConnexionActivity.TYPE_USER, ""));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        app = moduleList.getPackages();

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
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<Fragment> pages = new ArrayList<>(3);
        pages.add(new AccueilFragment());
        pages.add(new CatalogueFragment());
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void updateModuleList()
    {
        moduleList.update();
        this.app = moduleList.getAppList();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            Intent mainIntent = new Intent(this, ProfileActivity.class);
            this.startActivity(mainIntent);
        } else if (id == R.id.nav_website) {
            String url = "http://www.diabhelp.org";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent mainIntent = new Intent(this, GuideActivity.class);
            this.startActivity(mainIntent);
        } else if (id == R.id.nav_faq) {
            Intent mainIntent = new Intent(this, Faq.class);
            this.startActivity(mainIntent);
        } else if (id == R.id.nav_logout) {
            preocessLogout();
        } else if (id == R.id.nav_facebook) {
            String url = "https://www.facebook.com/diabhelp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
 /*       else if (id == R.id.nav_twitter) {
            // TODO Twitter account
            String url = "https://www.facebook.com/diabhelp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void preocessLogout() {
        this.unregisterFCMToken();
        SharedPreferences.Editor edit = _settings.edit();
        edit.putString(ConnexionActivity.TOKEN, "");
        edit.putString(ConnexionActivity.TYPE_USER, "");
        edit.putString(ConnexionActivity.ID_USER, "");
        edit.apply();
        Intent mainIntent = new Intent(this, ConnexionActivity.class);
        mainIntent.putExtra("logout", "true");
        this.startActivity(mainIntent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                launchParameters();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchParameters() {
        Intent intent = new Intent(this, ParametersActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_core, menu);
        return true;
    }

    public void unregisterFCMToken()
    {
        System.out.println("UNREGISTER TOKEN");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        intent.putExtra(RegistrationIntentService.ID_USER, _settings.getString(ConnexionActivity.ID_USER, null));
        intent.setAction(RegistrationIntentService.REMOVE_TOKEN);
        startService(intent);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}