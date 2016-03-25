package fr.diabhelp.diabhelp.API.Asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.diabhelp.diabhelp.API.ApiServices;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseRegister;
import fr.diabhelp.diabhelp.Connexion_inscription.RegisterActivity;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 26/01/2016.
 */

/**
 * Api async call to get session of user
 */
public class RegisterAPICallTask extends AsyncTask<String, Integer, ResponseRegister> {

    private String URL_API;

    private IApiCallTask _listener = null;
    private Context _context = null;
    private final Integer PARAM_USERNAME = 0;
    private final Integer PARAM_MAIL = 1;
    private final Integer PARAM_PASSWORD = 2;
    private final Integer PARAM_ROLE = 3;
    private final Integer PARAM_FIRSTNAME = 4;
    private final Integer PARAM_LASTNAME = 5;
    private ProgressDialog progress;

    public RegisterAPICallTask(Context context){
        _listener = (IApiCallTask) context;
        _context = context;
        URL_API = _context.getString(R.string.URL_API);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(true);
        progress.setMessage(_context.getString(R.string.register_loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseRegister doInBackground(String... params) {
        Call<ResponseBody> call = null;
        ResponseRegister responseRegister = null;

        ApiServices service = createService();
        call = service.register(params[PARAM_USERNAME], params[PARAM_MAIL], params[PARAM_PASSWORD], params[PARAM_ROLE], params[PARAM_FIRSTNAME], params[PARAM_LASTNAME]);

        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()) {
                Log.i("RegisterApiCallTask", "resultat de la requète : + [ " + reponse.raw() + "]");
                responseRegister = new ResponseRegister(JsonUtils.getObj(reponse.body().string()));
            }
            else {
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                responseRegister = new ResponseRegister();
                responseRegister.setError(RegisterActivity.Error.SERVER_ERROR);
            }
        }
        catch (IOException e) {
            progress.dismiss();
            if (responseRegister == null) {
                responseRegister = new ResponseRegister();
            }
            responseRegister.setError(RegisterActivity.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return (responseRegister);
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
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ResponseRegister responseRegister) {
        super.onPostExecute(responseRegister);
            _listener.onBackgroundTaskCompleted(responseRegister, "createAccount", progress);
        }
    }
