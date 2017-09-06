package fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet;

/**
 * Created by naqued on 28/09/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.Statistics.StatisticsAllFragment;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.Statistics.StatisticsMonthFragment;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.Statistics.StatisticsPersoFragment;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.Statistics.StatisticsWeekFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                StatisticsWeekFragment tab1 = new StatisticsWeekFragment();
                return tab1;
            case 1:
                StatisticsMonthFragment tab2 = new StatisticsMonthFragment();
                return tab2;
            case 2:
                StatisticsAllFragment tab3 = new StatisticsAllFragment();
                return tab3;
            case 3:
                StatisticsPersoFragment tab4 = new StatisticsPersoFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}