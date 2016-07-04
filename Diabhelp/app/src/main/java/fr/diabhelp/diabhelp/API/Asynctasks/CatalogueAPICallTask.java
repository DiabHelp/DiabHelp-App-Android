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
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 24/03/2016.
 */
public class CatalogueAPICallTask extends AsyncTask<Integer, String, ResponseCatalogue> {

    private String URL_API;

    private IApiCallTask _listener = null;
    private Context _context = null;
    private ProgressDialog progress;

    public CatalogueAPICallTask(Context context, IApiCallTask listener){
        _listener = listener;
        _context = context;
        URL_API = _context.getString(R.string.URL_API);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(true);
        progress.setMessage(_context.getString(R.string.catalogue_loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseCatalogue doInBackground(Integer... params) {
        Call<ResponseBody> call = null;
        ResponseCatalogue responseCatalogue = null;

        ApiServices service = createService();
        call= service.getModules();
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()){
                
                String body = reponse.body().string();
                Log.i(getClass().getSimpleName(),"modules du catalogue = " + body);
                responseCatalogue = new ResponseCatalogue(JsonUtils.getArray(body));
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseCatalogue = new ResponseCatalogue();
                responseCatalogue.setError(CatalogueFragment.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            progress.dismiss();
            if (responseCatalogue == null) {
                responseCatalogue = new ResponseCatalogue();
            }
            responseCatalogue.setError(CatalogueFragment.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return (responseCatalogue);
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
    protected void onPostExecute(ResponseCatalogue responseCatalogue) {
        super.onPostExecute(responseCatalogue);
        _listener.onBackgroundTaskCompleted(responseCatalogue, "getModules", progress);
    }
}
