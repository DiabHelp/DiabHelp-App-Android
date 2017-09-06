package fr.diabhelp.diabhelp.Carnet_de_suivi.API.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import fr.diabhelp.diabhelp.Carnet_de_suivi.API.ApiServices;
import fr.diabhelp.diabhelp.Carnet_de_suivi.API.IApiCallTask;
import fr.diabhelp.diabhelp.Carnet_de_suivi.API.Response.ResponseMail;
import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.Ressource.EntryToSend;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnetdesuivi;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import fr.diabhelp.diabhelp.R;
/**
 * Created by Sumbers on 29/03/2016.
 */
public class ExportAPICallTask extends AsyncTask<String, String, ResponseMail> {

    private String URL_API;
    private String URL_API_DEV;
    private ProgressDialog progress;
    private Context _context;
    private IApiCallTask _listener;
    private String _idUser;
    private ArrayList<EntryToSend> _entries;

    public ExportAPICallTask(Context context, IApiCallTask listener, String idUser, ArrayList<EntryToSend> entries) {
        this._context = context;
        this._listener = listener;
        this._idUser = idUser;
        this._entries = entries;
        URL_API = _context.getString(R.string.URL_API);
        URL_API_DEV = _context.getString(R.string.URL_API_dev);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(false);
        progress.setMessage(_context.getString(R.string.mail_loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseMail doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseMail reponseMail = null;
        ApiServices service = createService();
        call = service.sendEmail(_idUser, params[0], _entries);
        try{
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()) {
                reponseMail = new ResponseMail(JsonUtils.getObj(reponse.body().string()));
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                reponseMail= new ResponseMail();
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
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API_DEV)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return (retrofit.create(ApiServices.class));
    }

    @Override
    protected void onPostExecute(ResponseMail reponse) {
        super.onPostExecute(reponse);
        System.out.println("valeur de progress = " + progress);
        this._listener.onBackgroundTaskCompleted(reponse, "informOfsending", progress);
    }
}
