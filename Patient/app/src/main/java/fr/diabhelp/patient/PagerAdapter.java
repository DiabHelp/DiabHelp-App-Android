package fr.diabhelp.patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by 4kito on 02/08/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> _pages;

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
        super(fm);
        _pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return (_pages.get(position));
    }

    @Override
    public int getCount() {
        return _pages.size();
    }
}