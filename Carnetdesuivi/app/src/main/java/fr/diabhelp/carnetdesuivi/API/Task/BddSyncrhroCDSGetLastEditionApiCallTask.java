package fr.diabhelp.carnetdesuivi.API.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.carnetdesuivi.API.ApiServices;
import fr.diabhelp.carnetdesuivi.API.IApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSGetLastEdition;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class BddSyncrhroCDSGetLastEditionApiCallTask extends AsyncTask<String, String, ResponseCDSGetLastEdition> {

    private IApiCallTask listener;
    private Context context;
    private String URL_API = null;


    public BddSyncrhroCDSGetLastEditionApiCallTask(IApiCallTask listen, Context cont)
    {
        listener = listen;
        context = cont;
        URL_API = context.getString(R.string.URL_API);
    }


    @Override
    protected ResponseCDSGetLastEdition doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ApiServices service = null;
        ResponseCDSGetLastEdition responseCDS = null;
        service = createService();
        call = service.getLastEdition(params[0]);
        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess())
            {
                String body = reponse.body().string();
                responseCDS = new ResponseCDSGetLastEdition(JsonUtils.getObj(body));
            }
            else
            {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                if (responseCDS == null)
                    responseCDS = new ResponseCDSGetLastEdition();
                responseCDS.setError(Carnetdesuivi.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            if (responseCDS == null)
                responseCDS = new ResponseCDSGetLastEdition();
            responseCDS.setError(Carnetdesuivi.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return responseCDS;
    }

    @Override
    protected void onPostExecute(ResponseCDSGetLastEdition s) {
        super.onPostExecute(s);
        listener.onBackgroundTaskCompleted(s, "getLastEdition", null);
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
