package fr.diabhelp.carnetdesuivi.API.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import fr.diabhelp.carnetdesuivi.API.ApiServices;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseMail;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.API.IApiCallTask;
import fr.diabhelp.carnetdesuivi.R;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 29/03/2016.
 */
public class ExportAPICallTask extends AsyncTask<String, String, ResponseMail> {

    private String URL_API;
    private ProgressDialog progress;
    private Context _context;
    private IApiCallTask _listener;
    private ArrayList<EntryOfCDS> _entries;

    public ExportAPICallTask(Context context, IApiCallTask listener, ArrayList<EntryOfCDS> entries) {
        this._context = context;
        this._listener = listener;
        this._entries = entries;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(true);
        progress.setMessage(_context.getString(R.string.mail_loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseMail doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseMail reponseMail = null;
        ApiServices service = createService();
        call = service.sendEmail(params[0], _entries);
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()) {
                reponseMail = new ResponseMail(JsonUtils.getObj(reponse.body().string()));
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                new ResponseMail();
                reponseMail.setError(Carnetdesuivi.Error.SERVER_ERROR);
            }
        } catch (IOException e) {
            progress.dismiss();
            if (reponseMail == null){
                reponseMail = new ResponseMail();
            }
            reponseMail.setError(Carnetdesuivi.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return (reponseMail);
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
    protected void onPostExecute(ResponseMail reponse) {
        super.onPostExecute(reponse);
        this._listener.onBackgroundTaskCompleted(reponse, "informOfsending", progress);
    }
}
