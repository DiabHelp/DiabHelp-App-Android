package fr.diabhelp.diabhelp.Glucocompteur;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Simon on 25-Nov-15.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int _nbTab;

    public MyPagerAdapter(FragmentManager fm, int nbTab) {
        super(fm);
        _nbTab = nbTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CurrentMenuFragment currentMenuFragment = new CurrentMenuFragment();
                return currentMenuFragment;
            case 1:
                FavoritesMenusFragment favoritesMenusFragment = new FavoritesMenusFragment();
                return favoritesMenusFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return _nbTab;
    }
}
