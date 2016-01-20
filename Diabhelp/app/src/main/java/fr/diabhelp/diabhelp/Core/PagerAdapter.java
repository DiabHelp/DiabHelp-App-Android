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

        switch (position) {
            case 0:
                Accueil_Fragment tab1 = new Accueil_Fragment();
                return tab1;
            case 1:
                Catalogue_Fragment tab2 = new Catalogue_Fragment();
                return tab2;
            case 2:
                Parametres_Fragment tab3 = new Parametres_Fragment();
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