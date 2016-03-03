package fr.diabhelp.diabhelp.Connexion_inscription;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sumbers on 12/02/2016.
 */
public class MyPagerAdapter extends FragmentPagerAdapter{
    private final List<Fragment> _fragments;


    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this._fragments = fragments;
    }



    @Override
    public Fragment getItem(int position) {
        return (_fragments.get(position));
    }

    @Override
    public int getCount() {
        return (_fragments.size());
    }
}
