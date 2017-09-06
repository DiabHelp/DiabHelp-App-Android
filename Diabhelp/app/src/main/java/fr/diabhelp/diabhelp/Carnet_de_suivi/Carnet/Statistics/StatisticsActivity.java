package fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.Statistics;

/**
 * Created by vigour_a on 02/02/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.PagerAdapter;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnetdesuivi;
import fr.diabhelp.diabhelp.R;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistiques");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Entryintent = new Intent(StatisticsActivity.this, Carnetdesuivi.class);
                StatisticsActivity.this.startActivity(Entryintent);

                StatisticsActivity.this.finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Semaine"));
        tabLayout.addTab(tabLayout.newTab().setText("Mois"));
        tabLayout.addTab(tabLayout.newTab().setText("Tout"));
        tabLayout.addTab(tabLayout.newTab().setText("Perso"));
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
    public void onBackPressed() {

        Intent Entryintent = null;

        Entryintent = new Intent(StatisticsActivity.this, Carnetdesuivi.class);
        StatisticsActivity.this.startActivity(Entryintent);
        this.finish();

    }
}
