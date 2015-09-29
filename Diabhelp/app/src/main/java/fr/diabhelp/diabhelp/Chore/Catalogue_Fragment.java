package fr.diabhelp.diabhelp.Chore;

/**
 * Created by naqued on 28/09/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.diabhelp.diabhelp.R;

public class Catalogue_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.catalogue_fragment, container, false);
    }
}

