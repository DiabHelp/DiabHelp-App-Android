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
public class TokenPostAPICallTask extends AsyncTask<String, String, ResponseProfilSet> {

    private final String URL_API;

    private final Context _context;

    public static int USER_ID = 0;
    public static int TOKEN = 1;


    public TokenPostAPICallTask(Context context){
        _context = context;
        URL_API = _context.getString(R.string.URL_API_DEV);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponseProfilSet doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseProfilSet responseProfil = null;

        ApiServices service = createService();
        call = service.sentTokenBindWithUser(params[USER_ID], params[TOKEN]);
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()){

                String body = reponse.body().string();
                Log.i(getClass().getSimpleName(), "reponse set =  " + body);
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseProfil = new ResponseProfilSet();
                responseProfil.setError(ProfileActivity.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
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
    }
}

