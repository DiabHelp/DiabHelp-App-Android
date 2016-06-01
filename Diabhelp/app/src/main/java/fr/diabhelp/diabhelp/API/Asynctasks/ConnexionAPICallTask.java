package fr.diabhelp.diabhelp.API.Asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.net.ProtocolException;

import fr.diabhelp.diabhelp.API.ApiServices;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseConnexion;
import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.JsonUtils;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Sumbers on 26/01/2016.
 */

/**
 * Api async call to get session of user
 */
public class ConnexionAPICallTask extends AsyncTask<String, Integer, ResponseConnexion> {

    private String URL_API;

    private IApiCallTask _listener = null;
    private Context _context = null;
    private final Integer COOKIE_LENGTH = 26;
    private final Integer PARAM_USERNAME = 0;
    private final Integer PARAM_PASSWORD = 1;
    private ProgressDialog progress;

    public ConnexionAPICallTask(Context context){
        _listener = (IApiCallTask) context;
        _context = context;
        URL_API = _context.getString(R.string.URL_API);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(_context);
        progress.setCancelable(true);
        progress.setMessage("Connexion..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected ResponseConnexion doInBackground(String... params) {
        ResponseConnexion responseConnexion = null;
        Call<ResponseBody> call = null;

        ApiServices service = createService(params[PARAM_USERNAME] + ":" + params[PARAM_PASSWORD]);
        call = service.getBasicAuthSession(params[PARAM_USERNAME], params[PARAM_PASSWORD]);

        try {
                retrofit2.Response<ResponseBody> reponse = call.execute();
                Headers headers = reponse.headers();
                if (reponse.isSuccess()) {
                    System.out.println("headers");
                    String cookie = headers.get("set-Cookie");
                    cookie = cookie.substring(cookie.indexOf("=") + 1, (cookie.indexOf("=") + 1 + COOKIE_LENGTH));
                    responseConnexion = new ResponseConnexion(JsonUtils.getObj(reponse.body().string()));
                    responseConnexion.setCookie(cookie);
                }
                else {
                    Log.e("ConnexionApiCallTask", "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
                    responseConnexion = new ResponseConnexion();
                    responseConnexion.setError(ConnexionActivity.Error.SERVER_ERROR);
                }

        } catch (ProtocolException e)
        {
            progress.dismiss();
            if (responseConnexion == null){responseConnexion = new ResponseConnexion();}
            responseConnexion.setError(ConnexionActivity.Error.BAD_CREDENTIALS);
            e.printStackTrace();
        } catch (IOException e) {
            progress.dismiss();
            if (responseConnexion == null){responseConnexion = new ResponseConnexion();}
            responseConnexion.setError(ConnexionActivity.Error.SERVER_ERROR);
            e.printStackTrace();
        }
        return (responseConnexion);
    }

    private ApiServices createService(String credentials) {

        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return (chain.proceed(request));
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .client(client)
                .build();

        return (retrofit.create(ApiServices.class));
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ResponseConnexion responseConnexion) {
        super.onPostExecute(responseConnexion);
            _listener.onBackgroundTaskCompleted(responseConnexion, "initSession", progress);
        }
    }
