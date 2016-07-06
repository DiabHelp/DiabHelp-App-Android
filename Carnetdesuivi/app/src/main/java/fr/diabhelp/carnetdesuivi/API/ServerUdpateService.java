package fr.diabhelp.carnetdesuivi.API;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSGetLastEdition;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSetMissingEntries;
import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServerUdpateService extends IntentService {

    private String URL_API = null;
    public static final String EXTRA_ID_USER = "idUser";
    private String idUser = null;
    public DAO dao = null;
    private SQLiteDatabase db = null;
    Call<ResponseBody> call = null;
    ApiServices service = null;


    public ServerUdpateService() {
        super("update server");
    }



    @Override
    protected void onHandleIntent(Intent intent)
    {
       initVars(intent);

        ResponseCDSGetLastEdition responseCDS = null;
        call = service.getLastEdition(idUser);
        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()) {
                String body = reponse.body().string();
                responseCDS = new ResponseCDSGetLastEdition(JsonUtils.getObj(body));
                if (responseCDS.getError() == Carnetdesuivi.Error.NONE) {
                   compareDates(responseCDS.getLastEdition());
                }
            } else
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVars(Intent intent)
    {
        dao = DAO.getInstance(getApplicationContext());
        db = dao.open();
        Bundle bundle = intent.getExtras();
        idUser = bundle.getString(EXTRA_ID_USER);
        URL_API = getApplicationContext().getString(R.string.URL_API);
        service = createService();
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

    private void compareDates(Date lastEditionServer) {
        String lastEditionLocalStr = EntryOfCDSDAO.getLastEdition(idUser, db);
        try {
            Date lastEditionLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastEditionLocalStr);
            if (lastEditionServer.before(lastEditionLocal))
                sendMissingEntries(lastEditionServer.toString(), lastEditionLocal.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void sendMissingEntries(String from, String to)
    {
        ArrayList<EntryOfCDS> missingEntries = EntryOfCDSDAO.selectBetweenDays(from, to, idUser, db);
        ResponseCDSetMissingEntries responseCDS = null;
        call = service.setMissingEntries(idUser, missingEntries);
        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess())
            {
                String body = reponse.body().string();
                responseCDS = new ResponseCDSetMissingEntries();
            }
            else
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
