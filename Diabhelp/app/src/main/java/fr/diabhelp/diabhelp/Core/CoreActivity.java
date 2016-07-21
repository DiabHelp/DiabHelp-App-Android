package fr.diabhelp.diabhelp.Core;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
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
import fr.diabhelp.diabhelp.Models.ModuleList.PInfo;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;
import fr.diabhelp.diabhelp.R.string;
import fr.diabhelp.diabhelp.UtilizationGuide.GuideActivity;


public class CoreActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private ModuleList moduleList;
    private ArrayList<PInfo> app;
    private Boolean NetState;
    private TabLayout tabLayout;
    public static SharedPreferences _settings;



    public Boolean getNetState() {return NetState; }

    public void setNetStateAndChange(Boolean netstate) {
        NetState = netstate;
    // Change layout
        // apply Drawable.mutate().setColorFilter( 0xffff0000, Mode.MULTIPLY) on offline icone
        if (NetState == true)
            Toast.makeText(this, "Internet on", Toast.LENGTH_LONG).show();
        else if (NetState == false)
            Toast.makeText(this, "Internet off", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_core);
        this.moduleList = new ModuleList(this);
        CoreActivity._settings = this.getSharedPreferences(ConnexionActivity.PREF_FILE, Context.MODE_WORLD_READABLE);
        System.out.println("core ID_USER = " + CoreActivity._settings.getString(ConnexionActivity.ID_USER, ""));
        System.out.println("core ROLE = " + CoreActivity._settings.getString(ConnexionActivity.TYPE_USER, ""));
        Toolbar toolbar = (Toolbar) this.findViewById(id.toolbar);
        this.setSupportActionBar(toolbar);
        this.app = this.moduleList.getPackages();

        //init menu
        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) this.findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.tabLayout = (TabLayout) this.findViewById(id.tab_layout);
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Mes Outils"), 0);
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Catalogue"), 1);
        this.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) this.findViewById(id.pager);
        ArrayList<Fragment> pages = new ArrayList<>(3);
        pages.add(new AccueilFragment());
        pages.add(new CatalogueFragment());
        PagerAdapter adapter = new PagerAdapter
                (this.getSupportFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                try {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    public void updateModuleList()
    {
        this.moduleList.update();
        app = this.moduleList.getAppList();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == id.nav_profil) {
            Intent mainIntent = new Intent(this, ProfileActivity.class);
            startActivity(mainIntent);
        } else if (id == id.nav_website) {
            String url = "http://www.diabhelp.org";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            this.startActivity(intent);
        } else if (id == id.nav_help) {
            Intent mainIntent = new Intent(this, GuideActivity.class);
            startActivity(mainIntent);
        } else if (id == id.nav_faq) {
            Intent mainIntent = new Intent(this, Faq.class);
            startActivity(mainIntent);
        } else if (id == id.nav_logout) {
            Intent mainIntent = new Intent(this, ConnexionActivity.class);
            mainIntent.putExtra("logout", "true");
            startActivity(mainIntent);
            finish();
        } else if (id == id.nav_facebook) {
            String url = "https://www.facebook.com/diabhelp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            this.startActivity(intent);
        }
 /*       else if (id == R.id.nav_twitter) {
            // TODO Twitter account
            String url = "https://www.facebook.com/diabhelp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }*/

        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case id.settings:
                this.launchParameters();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchParameters() {
        Intent intent = new Intent(this, ParametersActivity.class);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(menu.menu_core, menu);
        return true;
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        Editor edit = CoreActivity._settings.edit();
        edit.putString(ConnexionActivity.TOKEN, "");
//        edit.putString(ConnexionActivity.TYPE_USER, "");
//        edit.putString(ConnexionActivity.ID_USER, "");
        edit.commit();
    }
}