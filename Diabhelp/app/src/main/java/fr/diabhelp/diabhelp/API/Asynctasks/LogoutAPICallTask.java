package fr.diabhelp.diabhelp.API.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.diabhelp.API.ApiServices;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseCatalogue;
import fr.diabhelp.diabhelp.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 21/04/2016.
 */
public class LogoutAPICallTask extends AsyncTask<String, Integer, Boolean> {

    private String URL_API;

    private IApiCallTask _listener = null;
    private Context _context = null;
    private final Integer PARAM_TOKEN = 0;

    public LogoutAPICallTask(IApiCallTask _listener, Context context) {
        this._listener = _listener;
        this._context = context;
        URL_API = _context.getString(R.string.URL_API_DEV);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Call<ResponseBody> call = null;
        Response<ResponseBody> response = null;
        ApiServices service = createService();
        Boolean work = false;
/*        call =  service.logout(params[PARAM_TOKEN]);*/
        try {
            response = call.execute();
            if (response.isSuccess()) {
                work = true;
            }
            else{
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + response.code() + "\n message d'erreur = " + response.errorBody().string());
                work = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (true);
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
    protected void onPostExecute(Boolean work)
    {
        super.onPostExecute(work);
        _listener.onBackgroundTaskCompleted(work, "bringBackToConnexionActivity", null);
    }
}
