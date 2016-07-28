package fr.diabhelp.diabhelp.API.Asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.diabhelp.API.ApiServices;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilSet;
import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by naqued on 05/07/16.
 */
public class ProfilPostAPICallTask extends AsyncTask<String, String, ResponseProfilSet> {

    private final String URL_API;

    private final IApiCallTask _listener;
    private final Context _context;
    private ProgressDialog progress;

    public static int ID;
    public static int PASSWORD = 7;
    public static int EMAIL = 1;
    public static int FIRSTNAME = 3;
    public static int LASTNAME = 2;
    public static int PHONE = 4;
    public static int BIRTHDATE = 5;
    public static int ORGANISME = 6;


    public ProfilPostAPICallTask(Context context, IApiCallTask listener){
        _listener = listener;
        _context = context;
        URL_API = _context.getString(R.string.URL_API);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(false);
        progress.setMessage(_context.getString(R.string.profile_save_loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseProfilSet doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseProfilSet responseProfil = null;

        ApiServices service = createService();
        call = service.setInfo(params[ID], params[EMAIL], params[FIRSTNAME], params[LASTNAME], params[PHONE], params[BIRTHDATE], params[ORGANISME], params[PASSWORD]);
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()){

                String body = reponse.body().string();
                Log.i(getClass().getSimpleName(), "reponse set =  " + body);
                responseProfil = new ResponseProfilSet();
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseProfil = new ResponseProfilSet();
                responseProfil.setError(ProfileActivity.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            progress.dismiss();
            if (responseProfil == null) {
                responseProfil = new ResponseProfilSet();
            }
            responseProfil.setError(ProfileActivity.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return responseProfil;
    }

    private ApiServices createService() {
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiServices.class);
    }

    @Override
    protected void onPostExecute(ResponseProfilSet responseProfil) {
        super.onPostExecute(responseProfil);
        _listener.onBackgroundTaskCompleted(responseProfil, "setInfo", progress);
    }
}

