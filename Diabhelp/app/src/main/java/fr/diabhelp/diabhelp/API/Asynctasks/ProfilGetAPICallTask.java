package fr.diabhelp.diabhelp.API.Asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.diabhelp.API.ApiServices;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilGet;
import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by naqued on 05/07/16.
 */
public class ProfilGetAPICallTask extends AsyncTask<String, String, ResponseProfilGet> {

    private String URL_API;

    private IApiCallTask _listener = null;
    private Context _context = null;
    private ProgressDialog progress;

    public ProfilGetAPICallTask(Context context, IApiCallTask listener){
        _listener = listener;
        _context = context;
        URL_API = _context.getString(R.string.URL_API);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(false);
        progress.setMessage(_context.getString(R.string.profile_loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseProfilGet doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseProfilGet responseProfil = null;

        ApiServices service = createService();
        call = service.getInfo(params[0]);
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()){

                String body = reponse.body().string();
                Log.i(getClass().getSimpleName(), "user =  " + body);
                responseProfil = new ResponseProfilGet(JsonUtils.getObj(body));
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseProfil = new ResponseProfilGet();
                responseProfil.setError(ProfileActivity.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            if (responseProfil == null) {
                responseProfil = new ResponseProfilGet();
            }
            responseProfil.setError(ProfileActivity.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return(responseProfil);
    }

    private ApiServices createService() {
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return (retrofit.create(ApiServices.class));
    }

    @Override
    protected void onPostExecute(ResponseProfilGet responseProfil) {
        super.onPostExecute(responseProfil);
        _listener.onBackgroundTaskCompleted(responseProfil, "getInfo", progress);
    }

}

