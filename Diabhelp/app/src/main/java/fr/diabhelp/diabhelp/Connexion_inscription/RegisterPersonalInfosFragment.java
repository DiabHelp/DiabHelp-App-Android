package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import fr.diabhelp.diabhelp.MyToast;
import fr.diabhelp.diabhelp.R;

public class RegisterPersonalInfosFragment extends Fragment {

    private TextView firstnameView;
    private TextView lastnameView;
    private Spinner roleSpinner;
    private Button ValidateButton;

    private Activity _context;
    private FragmentSecondStepListener mListener;

    public RegisterPersonalInfosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_personal_infos, container, false);
        firstnameView = (TextView) view.findViewById(R.id.firstname_input);
        lastnameView = (TextView) view.findViewById(R.id.lastname_input);
        roleSpinner = (Spinner) view.findViewById(R.id.role_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(_context, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        ValidateButton = (Button) view.findViewById(R.id.validate_button);
        ValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname, lastname;
                String role;
                FieldError fieldError;
                firstname = firstnameView.getText().toString();
                lastname = lastnameView.getText().toString();
                role = (String) roleSpinner.getSelectedItem();
                fieldError = checkFields(firstname, lastname);
                if (fieldError != FieldError.NONE) {
                    manage_fieldError(fieldError);
                } else {
                    role = formatRole(role);
                    mListener.saveDatasSecondStep(firstname, lastname, role);
                }
            }
        });
        return (view);
    }

    private String formatRole(String role) {
        String[] roles = getResources().getStringArray(R.array.roles);
        if (role.equals(roles[0])){
            role = "ROLE_PATIENT";
        }
        else if (role.equals(roles[1])){
            role = "ROLE_PROCHE";
        }
        return (role);
    }

    private void manage_fieldError(FieldError fieldError) {
        switch (fieldError)
        {
            case FIELD_INCOMPLETE:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_incomplete_fields), Toast.LENGTH_LONG, _context);
                Log.i("RegisterPersonalInfos", "Les champs sont incomplets");
                break;
            }
        }
    }

    private FieldError checkFields(String firstname, String lastname) {
        FieldError error;
        if (!isStringValid(firstname, 3) || !isStringValid(lastname, 3))
            error = FieldError.FIELD_INCOMPLETE;
        else
            error = FieldError.NONE;
        return  (error);
    }

    private Boolean isStringValid(String string, int lenghtMinRequired)
    {
        Boolean is = false;
        if (!string.isEmpty() && string.length() >= lenghtMinRequired)
            is = true;
        return (is);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof FragmentSecondStepListener) {
            mListener = (FragmentSecondStepListener) context;
            _context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentSecondStepListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface FragmentSecondStepListener {

        void saveDatasSecondStep(String firstname, String lastName, String role);
    }

    public enum FieldError{
        NONE,
        FIELD_INCOMPLETE,
    }
}
