package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> pages;

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
        super(fm);
        this.pages = new ArrayList<Fragment>(pages);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        fragment = this.pages.get(position);
        return (fragment);
    }

    @Override
    public int getCount() {
        return this.pages.size();
    }
}