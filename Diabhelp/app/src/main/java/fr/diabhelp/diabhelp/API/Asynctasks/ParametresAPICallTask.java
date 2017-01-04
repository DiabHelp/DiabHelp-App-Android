package fr.diabhelp.diabhelp.API.Asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.diabhelp.API.ApiServices;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseCatalogue;
import fr.diabhelp.diabhelp.Core.CatalogueFragment;
import fr.diabhelp.diabhelp.R.string;
import fr.diabhelp.diabhelp.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

/**
 * Created by Sumbers on 24/03/2016.
 */
public class ParametresAPICallTask extends AsyncTask<String, String, ResponseCatalogue> {

    private final String URL_API;

    private IApiCallTask _listener;
    private Context _context;
    private ProgressDialog progress;

    public ParametresAPICallTask(Context context, IApiCallTask listener){
        this._listener = listener;
        this._context = context;
        this.URL_API = this._context.getString(string.URL_API_DEV);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponseCatalogue doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseCatalogue responseCatalogue = null;

        ApiServices service = this.createService();
        call = service.getModules();
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()){
                
                String body = reponse.body().string();
                Log.i(this.getClass().getSimpleName(),"modules du catalogue = " + body);
                responseCatalogue = new ResponseCatalogue(JsonUtils.getArray(body));
            }
            else {
                Log.e(this.getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseCatalogue = new ResponseCatalogue();
                responseCatalogue.setError(CatalogueFragment.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            this.progress.dismiss();
            if (responseCatalogue == null) {
                responseCatalogue = new ResponseCatalogue();
            }
            responseCatalogue.setError(CatalogueFragment.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return responseCatalogue;
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
    protected void onPostExecute(ResponseCatalogue responseCatalogue) {
        super.onPostExecute(responseCatalogue);
        this._listener.onBackgroundTaskCompleted(responseCatalogue, "getModules", this.progress);
    }
}
