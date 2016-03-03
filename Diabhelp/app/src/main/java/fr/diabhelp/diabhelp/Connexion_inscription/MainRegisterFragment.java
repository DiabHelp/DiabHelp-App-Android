package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.diabhelp.diabhelp.R;

public class MainRegisterFragment extends Fragment {

    private Button mainRegisterButton;
    private Button facebookRegisterButton;

    private MainFragmentListener mListener;
    private Activity _context;

    public MainRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_register, container, false);
        mainRegisterButton = (Button) view.findViewById(R.id.signup_button);
        mainRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisteNatifClick();
            }
        });
        facebookRegisterButton = (Button) view.findViewById(R.id.signup_facebook_button);
        return (view);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof MainFragmentListener) {
            mListener = (MainFragmentListener) context;
            _context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragmentListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MainFragmentListener {
        void onRegisteNatifClick();
    }
}
