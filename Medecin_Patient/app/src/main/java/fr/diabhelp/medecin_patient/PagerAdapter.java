package fr.diabhelp.medecin_patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by sundava on 09/03/16.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int _nbTab;

    public PagerAdapter(FragmentManager fm, int nbTab) {
        super(fm);
        _nbTab = nbTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainActivityFragment mainActivityFragment = new MainActivityFragment();
                return mainActivityFragment;
            case 1:
                MedecinsFragment medecinsListFragment = new MedecinsFragment();
                return medecinsListFragment;
            case 2:
                ParametresFragment parametresFragment = new ParametresFragment();
                return parametresFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return _nbTab;
    }
}

