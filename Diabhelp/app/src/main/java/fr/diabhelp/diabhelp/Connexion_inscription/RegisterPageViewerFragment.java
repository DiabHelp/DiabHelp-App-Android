package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.R;

public class RegisterPageViewerFragment extends Fragment {
    private Activity _context;
    private List<Fragment> _fragments;
    private MyPagerAdapter myPagerAdapter;
    private NonSwipeableViewPager mViewPager;
    private String str;

    public RegisterPageViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("création a nouveau du fragment");
        _fragments = new ArrayList<Fragment>();
        _fragments.add(new RegisterConnexionInfosFragment());
        _fragments.add(new RegisterPersonalInfosFragment());
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), _fragments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_register_page_viewer, container, false);
        mViewPager = (NonSwipeableViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setCurrentItem(0);
        return (view);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
            _context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
