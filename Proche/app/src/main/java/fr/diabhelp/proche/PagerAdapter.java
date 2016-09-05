package fr.diabhelp.proche;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 4kito on 02/08/2016.
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
                SuivisFragment suivisFragment = new SuivisFragment();
                return suivisFragment;
            case 1:
                DemandesFragment demandesFragment = new DemandesFragment();
                return demandesFragment;
            case 2:
                RechercheFragment rechercheFragment = new RechercheFragment();
                return rechercheFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return _nbTab;
    }
}
