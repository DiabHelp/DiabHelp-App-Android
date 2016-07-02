package fr.diabhelp.carnetdesuivi.API.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import fr.diabhelp.carnetdesuivi.API.ApiServices;
import fr.diabhelp.carnetdesuivi.API.IApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSetMissingEntries;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class BddSyncrhroCDSetMissingEntriesApiCallTask extends AsyncTask<String, String, ResponseCDSetMissingEntries> {

    private IApiCallTask listener;
    private Context context;
    private String URL_API = null;
    private ArrayList<EntryOfCDS> entryOfCDSList;


    public BddSyncrhroCDSetMissingEntriesApiCallTask(IApiCallTask listen, Context cont, ArrayList<EntryOfCDS> entries)

    {
        listener = listen;
        context = cont;
        entryOfCDSList = entries;
        URL_API = context.getString(R.string.URL_API);
    }


    @Override
    protected ResponseCDSetMissingEntries doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ApiServices service = null;
        ResponseCDSetMissingEntries responseCDS = null;
        service = createService();
        call = service.setMissingEntries(params[0], entryOfCDSList);
        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess())
            {
                String body = reponse.body().string();
                responseCDS = new ResponseCDSetMissingEntries();
            }
            else
            {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                if (responseCDS == null)
                    responseCDS = new ResponseCDSetMissingEntries();
                responseCDS.setError(Carnetdesuivi.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            if (responseCDS == null)
                responseCDS = new ResponseCDSetMissingEntries();
            responseCDS.setError(Carnetdesuivi.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return responseCDS;
    }

    @Override
    protected void onPostExecute(ResponseCDSetMissingEntries s) {
        super.onPostExecute(s);
        listener.onBackgroundTaskCompleted(s, "setMissingEntries", null);
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
}
