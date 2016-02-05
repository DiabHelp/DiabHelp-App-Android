package fr.diabhelp.carnetdesuivi.Carnet;

/**
 * Created by naqued on 28/09/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                StatisticsDayFragment tab1 = new StatisticsDayFragment();
                return tab1;
            case 1:
                StatisticsMonthFragment tab2 = new StatisticsMonthFragment();
                return tab2;
            case 2:
                StatisticsAllFragment tab3 = new StatisticsAllFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}