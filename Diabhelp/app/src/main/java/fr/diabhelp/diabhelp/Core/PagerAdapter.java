package fr.diabhelp.diabhelp.Core;

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
        Fragment fragment = null;
        System.out.println("pagerview position actuelle = " + position);
        switch (position) {
            case 0:
                fragment = new AccueilFragment();
                break;
            case 1:
                fragment = new CatalogueFragment();
                break;
            case 2:
                fragment = new ParametresFragment();
                break;
           }
        return (fragment);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}