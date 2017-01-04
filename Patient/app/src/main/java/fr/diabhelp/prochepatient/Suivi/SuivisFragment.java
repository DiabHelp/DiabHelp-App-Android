package fr.diabhelp.prochepatient.Suivi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import fr.diabhelp.prochepatient.ApiLinker.ApiErrors;
import fr.diabhelp.prochepatient.ApiLinker.ApiService;
import fr.diabhelp.prochepatient.ApiLinker.ResponseSuivi;
import fr.diabhelp.prochepatient.ApiLinker.RetrofitHelper;
import fr.diabhelp.prochepatient.Suividesproches;
import fr.diabhelp.prochepatient.R;
import fr.diabhelp.prochepatient.User;
import fr.diabhelp.prochepatient.Utils.DateUtils;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import fr.diabhelp.prochepatient.Utils.MyToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 4kito on 02/08/2016.
 */
public class SuivisFragment extends Fragment implements ExpandableListAdapterListener {

    private Suividesproches _parent;
    String                                          _idUser;
    User.Role                                       _role;
    private RecyclerView                            _recyclerView;
    private RecyclerView.LayoutManager              _recLayoutManager;
    private ArrayList<ExpandableListAdapter.Item>   _userItems = new ArrayList<>();
    protected ExpandableListAdapter                   _recAdapter;
    private FloatingActionButton                    fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _parent = (Suividesproches) getActivity();
        _idUser = _parent.getIdUser();
        _role = _parent.getRole();
        System.out.println("onCreate role = " + _role);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_suivis, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.global_alert_button);
        fab.setVisibility(View.GONE);
        _recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    _userItems = getUserItems();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
                _recyclerView.setAdapter(new ExpandableListAdapter(_userItems, _role, (ExpandableListAdapterListener) getParentFragment()));
            }
        });
        try {
            _userItems = getUserItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _recyclerView.setAdapter(new ExpandableListAdapter(_userItems, _role, this));
        return v;
    }

    public ArrayList<ExpandableListAdapter.Item> getUserItems() throws Exception {
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.PROD);
        Call<ResponseBody> call = null;
        System.out.println("getUserItems role = " + _role);
        if (_role.equals(User.Role.PATIENT) == true)
            call = service.getAllProches(_idUser);
        else if (_role.equals(User.Role.PROCHE) == true)
            call = service.getAllPatients(_idUser);
        if (call != null)
        {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try
                    {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            ResponseSuivi rep = new ResponseSuivi(JsonUtils.getObj(body), _role);
                            displayEntries(rep);
                            //displayFloatingButton();
                            if (_role.equals(User.Role.PATIENT) && rep.getUserList().size() > 0) {
                              displayFloatingButton();
                            }
                        } else {
                            String error = response.errorBody().string();
                            System.out.println("erreur message = " + error);
                            ApiErrors apiError = ApiErrors.getFromMessage(error);
                            manageError(apiError);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        ApiErrors api = ApiErrors.NETWORK_ERROR;
                        manageError(api);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                    manageError(apiError);
                }
            });
        }
        return _userItems;
    }

    /*DISPLAY FLOATING BUTTON WHEN USER IS PATIENT AND HAVE SOME CONTACTS*/
    public void displayFloatingButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setView(R.layout.alert_dialog);
                alertDialogBuilder.setTitle("Envoyer une demande d'assistance à tous vos contacts");
                alertDialogBuilder.setMessage("Ecrivez une description de votre problème");
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ENVOYER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText alertEdit =  (EditText) alertDialog.findViewById(R.id.alert_message);
                        String alertText = alertEdit.getText().toString();
                        Date now = new Date();
                        SimpleDateFormat sd = new SimpleDateFormat(DateUtils.SERVER_DATE_FORMAT);
                        String nowStr = sd.format(now);
                        alertAllProches(nowStr, alertText);
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return;
            }
        });
        fab.setVisibility(View.VISIBLE);
    }

    private void displayEntries(ResponseSuivi rep) {
        if (_recAdapter == null) {
            _recAdapter = new ExpandableListAdapter(rep.getUserList(), _role, this);
        } else {
            _recAdapter.setPatientsList(rep.getUserList());
        }
        _recyclerView.setAdapter(_recAdapter);
    }

    protected void manageError(ApiErrors error) {
        switch (error) {
            case NETWORK_ERROR: {
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, _parent);
                Log.e("RechercheFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR: {
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, _parent);
                Log.e("RechercheFragment", "Erreur lors du traitement de la demande");
                break;
            }
        }
    }

    private void alertAllProches(String nowStr, String alertText) {
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.sendAlertToAllContacts(this._idUser, nowStr, alertText);
        if (call != null)
        {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try
                    {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                        } else {
                            String error = response.errorBody().string();
                            System.out.println("erreur message = " + error);
                            //ApiErrors apiError = ApiErrors.getFromMessage(error);
                            // manageError(apiError);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //ApiErrors api = ApiErrors.NETWORK_ERROR;
                        //manageError(api);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                    // manageError(apiError);
                }
            });
        }
    }


    /*HANDLE CLICK ON SPECIFIC ALERT BUTTON ON A LISTE OF PROCHES*/
    @Override
    public void onClickAlertSpecificProche(String idProche, String alertText, String nowStr) {
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.PROD);
        Call<ResponseBody> call = service.sendAlertToSpecificUser(this._idUser, idProche, nowStr, alertText);
        if (call != null)
        {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try
                    {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                        } else {
                            String error = response.errorBody().string();
                            System.out.println("erreur message = " + error);
                            //ApiErrors apiError = ApiErrors.getFromMessage(error);
                           // manageError(apiError);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //ApiErrors api = ApiErrors.NETWORK_ERROR;
                        //manageError(api);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                   // manageError(apiError);
                }
            });
        }
    }
}

