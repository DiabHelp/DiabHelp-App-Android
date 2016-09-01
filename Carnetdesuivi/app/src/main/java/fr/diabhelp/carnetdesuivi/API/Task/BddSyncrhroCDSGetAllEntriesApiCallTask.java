package fr.diabhelp.carnetdesuivi.API.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.carnetdesuivi.API.ApiServices;
import fr.diabhelp.carnetdesuivi.API.IApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSGetAllEntries;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class BddSyncrhroCDSGetAllEntriesApiCallTask extends AsyncTask<String, String, ResponseCDSGetAllEntries> {

    private IApiCallTask listener;
    private Context context;
    private String URL_API = null;
    private ProgressDialog progress;


    public BddSyncrhroCDSGetAllEntriesApiCallTask(IApiCallTask listen, Context cont)
    {
        listener = listen;
        context = cont;
        URL_API = context.getString(R.string.URL_API_dev);
    }


//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progress = new ProgressDialog(context);
//        progress.setCancelable(false);
//        progress.setMessage(context.getString(R.string.synchro_bd));
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.show();
//    }

    @Override
    protected ResponseCDSGetAllEntries doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ApiServices service = null;
        ResponseCDSGetAllEntries responseCDS = null;
        service = createService();
        call = service.getAllEntries(params[0]);
        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess())
            {
                String body = reponse.body().string();
                responseCDS = new ResponseCDSGetAllEntries(JsonUtils.getObj(body));
            }
            else
            {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                if (responseCDS == null)
                    responseCDS = new ResponseCDSGetAllEntries();
                responseCDS.setError(Carnetdesuivi.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            if (responseCDS == null)
                responseCDS = new ResponseCDSGetAllEntries();
            responseCDS.setError(Carnetdesuivi.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return responseCDS;
    }

    @Override
    protected void onPostExecute(ResponseCDSGetAllEntries s) {
        super.onPostExecute(s);
        listener.onBackgroundTaskCompleted(s, "getEntries", null);
    }

    private ApiServices createService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return (retrofit.create(ApiServices.class));
    }
}
