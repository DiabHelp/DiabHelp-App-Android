package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.diabhelp.diabhelp.MyToast;
import fr.diabhelp.diabhelp.R;

public class RegisterConnexionInfosFragment extends Fragment {

    private TextView mailView;
    private TextView loginView;
    private TextView pwdView;
    private TextView pwdConfirmationView;
    private Button nextStepButton;

    private Activity _context;
    private FragmentFirstStepListener mListener;

    public RegisterConnexionInfosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_connexion_infos, container, false);
        mailView = (TextView) view.findViewById(R.id.email_input);
        loginView = (TextView) view.findViewById(R.id.login_input);
        pwdView = (TextView) view.findViewById(R.id.pwd_input);
        pwdConfirmationView = (TextView) view.findViewById(R.id.pwdconfirm_input);
        nextStepButton = (Button) view.findViewById(R.id.next_step_button);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail, login, pwd, pwdConfirm;
                FieldError fieldError;
                mail = mailView.getText().toString();
                login = loginView.getText().toString();
                pwd = pwdView.getText().toString();
                pwdConfirm= pwdConfirmationView.getText().toString();
                fieldError = checkFields(mail, login, pwd, pwdConfirm);
                if (fieldError != FieldError.NONE) {
                    manage_fieldError(fieldError);
                }
                else{
                    mListener.saveDatasFirstStep(mail, login, pwd);
                }
            }
        });
        return (view);
    }

    private void manage_fieldError(FieldError fieldError) {
        switch (fieldError)
        {
            case FIELD_INCOMPLETE:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_incomplete_fields), Toast.LENGTH_LONG, _context);
                Log.i("RegisterConnexionInfos", "Les champs sont incomplets");
                break;
            }
            case WRONG_MAIL_FORMAT:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_mail_format_incorrect), Toast.LENGTH_LONG, _context);
                Log.i("RegisterConnexionInfos", "le mail à un format incorrect");
                break;
            }
            case PASSWORDS_DIFFER:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_passwords_differ), Toast.LENGTH_LONG, _context);
                Log.i("RegisterConnexionInfos", "les mots de passe sont différents");
                break;
            }
        }
    }

    private FieldError checkFields(String mail, String login, String pwd, String pwdConfirmation) {
        FieldError error;
        if (!isStringValid(mail, 0) || !isStringValid(login, 6) || !isStringValid(pwd, 6) || !isStringValid(pwd, 6) || !isStringValid(pwdConfirmation, 6))
            error = FieldError.FIELD_INCOMPLETE;
        else if (!isEmailValid(mail))
            error = FieldError.WRONG_MAIL_FORMAT;
        else if (!pwd.equals(pwdConfirmation))
            error = FieldError.PASSWORDS_DIFFER;
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

    Boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof FragmentFirstStepListener) {
            mListener = (FragmentFirstStepListener) context;
            _context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentFirstStepListener");
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
    public interface FragmentFirstStepListener {

        void saveDatasFirstStep(String mail, String login, String pwd);
    }

    public enum FieldError{
        NONE,
        TOO_SHORT_INPUT,
        FIELD_INCOMPLETE,
        PASSWORDS_DIFFER,
        WRONG_MAIL_FORMAT
    }
}
