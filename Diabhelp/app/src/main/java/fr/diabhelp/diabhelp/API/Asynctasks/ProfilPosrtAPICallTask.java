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
import fr.diabhelp.diabhelp.R.string;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

/**
 * Created by naqued on 05/07/16.
 */
public class ProfilPosrtAPICallTask extends AsyncTask<String, String, ResponseProfilSet> {

    private final String URL_API;

    private IApiCallTask _listener;
    private Context _context;
    private ProgressDialog progress;

    public static int ID;
    public static int PASSWORD = 1;
    public static int EMAIL = 2;
    public static int FIRSTNAME = 3;
    public static int LASTNAME = 4;
    public static int PHONE = 5;
    public static int BIRTHDATE = 6;
    public static int ORGANISME = 7;


    public ProfilPosrtAPICallTask(Context context, IApiCallTask listener){
        this._listener = listener;
        this._context = context;
        this.URL_API = this._context.getString(string.URL_API);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progress = new ProgressDialog(this._context);
        this.progress.setCancelable(false);
        this.progress.setMessage(this._context.getString(string.profile_save_loading));
        this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progress.show();
    }

    @Override
    protected ResponseProfilSet doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseProfilSet responseProfil = null;

        System.out.println("id dans l'async = " + params[ProfilPosrtAPICallTask.ID]);
        ApiServices service = this.createService();
        call = service.setInfo(params[ProfilPosrtAPICallTask.ID], params[ProfilPosrtAPICallTask.EMAIL], params[ProfilPosrtAPICallTask.FIRSTNAME], params[ProfilPosrtAPICallTask.LASTNAME], params[ProfilPosrtAPICallTask.PHONE], params[ProfilPosrtAPICallTask.BIRTHDATE], params[ProfilPosrtAPICallTask.ORGANISME], params[ProfilPosrtAPICallTask.PASSWORD]);
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()){

                String body = reponse.body().string();
                Log.i(this.getClass().getSimpleName(), "reponse set =  " + body);
                responseProfil = new ResponseProfilSet();
            }
            else {
                Log.e(this.getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseProfil = new ResponseProfilSet();
                responseProfil.setError(ProfileActivity.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            this.progress.dismiss();
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
        Retrofit retrofit = new Builder()
                .baseUrl(this.URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiServices.class);
    }

    @Override
    protected void onPostExecute(ResponseProfilSet responseProfil) {
        super.onPostExecute(responseProfil);
        this._listener.onBackgroundTaskCompleted(responseProfil, "setInfo", this.progress);
    }
}

