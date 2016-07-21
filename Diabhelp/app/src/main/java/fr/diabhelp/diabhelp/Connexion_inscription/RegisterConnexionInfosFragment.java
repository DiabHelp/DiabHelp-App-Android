package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;
import fr.diabhelp.diabhelp.Utils.MyToast;

public class RegisterConnexionInfosFragment extends Fragment {

    private TextView mailView;
    private TextView loginView;
    private TextView pwdView;
    private TextView pwdConfirmationView;
    private Button nextStepButton;

    private Activity _context;
    private RegisterConnexionInfosFragment.FragmentFirstStepListener mListener;

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
        View view = inflater.inflate(layout.fragment_register_connexion_infos, container, false);
        this.mailView = (TextView) view.findViewById(id.email_input);
        this.loginView = (TextView) view.findViewById(id.login_input);
        this.pwdView = (TextView) view.findViewById(id.pwd_input);
        this.pwdConfirmationView = (TextView) view.findViewById(id.pwdconfirm_input);
        this.nextStepButton = (Button) view.findViewById(id.next_step_button);
        this.nextStepButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail, login, pwd, pwdConfirm;
                RegisterConnexionInfosFragment.FieldError fieldError;
                mail = RegisterConnexionInfosFragment.this.mailView.getText().toString();
                login = RegisterConnexionInfosFragment.this.loginView.getText().toString();
                pwd = RegisterConnexionInfosFragment.this.pwdView.getText().toString();
                pwdConfirm= RegisterConnexionInfosFragment.this.pwdConfirmationView.getText().toString();
                fieldError = RegisterConnexionInfosFragment.this.checkFields(mail, login, pwd, pwdConfirm);
                if (fieldError != RegisterConnexionInfosFragment.FieldError.NONE) {
                    RegisterConnexionInfosFragment.this.manage_fieldError(fieldError);
                }
                else{
                    RegisterConnexionInfosFragment.this.mListener.saveDatasFirstStep(mail, login, pwd);
                }
            }
        });
        return view;
    }

    private void manage_fieldError(RegisterConnexionInfosFragment.FieldError fieldError) {
        switch (fieldError)
        {
            case FIELD_INCOMPLETE:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_incomplete_fields), Toast.LENGTH_LONG, _context);
                Log.i("RegisterConnexionInfos", "Les champs sont incomplets");
                break;
            case WRONG_MAIL_FORMAT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_mail_format_incorrect), Toast.LENGTH_LONG, _context);
                Log.i("RegisterConnexionInfos", "le mail à un format incorrect");
                break;
            case PASSWORDS_DIFFER:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_passwords_differ), Toast.LENGTH_LONG, _context);
                Log.i("RegisterConnexionInfos", "les mots de passe sont différents");
                break;
        }
    }

    private RegisterConnexionInfosFragment.FieldError checkFields(String mail, String login, String pwd, String pwdConfirmation) {
        RegisterConnexionInfosFragment.FieldError error;
        if (!this.isStringValid(mail, 0) || !this.isStringValid(login, 6) || !this.isStringValid(pwd, 6) || !this.isStringValid(pwdConfirmation, 6))
            error = RegisterConnexionInfosFragment.FieldError.FIELD_INCOMPLETE;
        else if (!this.isEmailValid(mail))
            error = RegisterConnexionInfosFragment.FieldError.WRONG_MAIL_FORMAT;
        else if (!pwd.equals(pwdConfirmation))
            error = RegisterConnexionInfosFragment.FieldError.PASSWORDS_DIFFER;
        else
            error = RegisterConnexionInfosFragment.FieldError.NONE;
        return error;
    }

    private Boolean isStringValid(String string, int lenghtMinRequired)
    {
        Boolean is = false;
        if (!string.isEmpty() && string.length() >= lenghtMinRequired)
            is = true;
        return is;
    }

    Boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof RegisterConnexionInfosFragment.FragmentFirstStepListener) {
            this.mListener = (RegisterConnexionInfosFragment.FragmentFirstStepListener) context;
            this._context = context;
        } else {
            throw new RuntimeException(context
                    + " must implement FragmentFirstStepListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
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
