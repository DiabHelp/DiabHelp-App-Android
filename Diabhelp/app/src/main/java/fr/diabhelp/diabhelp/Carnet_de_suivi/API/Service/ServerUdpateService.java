package fr.diabhelp.diabhelp.Carnet_de_suivi.API.Service;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import fr.diabhelp.diabhelp.Carnet_de_suivi.API.ApiServices;
import fr.diabhelp.diabhelp.Carnet_de_suivi.API.Response.ResponseCDSetMissingEntries;
import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.DAO;
import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.Ressource.EntryToSend;
import fr.diabhelp.diabhelp.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServerUdpateService extends IntentService {

    public static final String EXTRA_ACTION ="action";
    public static final String EXTRA_ID_USER = "idUser";
    public static final String EXTRA_ID_ENTRY = "id";
    public static short ADD = 0;
    public static short UPDATE = 1;
    public static short DELETE = 2;
    public DAO dao = null;
    Call<ResponseBody> call = null;
    ApiServices service = null;
    private String URL_API = null;
    private String URL_API_DEV = null;
    private short action = 0;
    private Integer idEntry = null;
    private String idUser = null;
    private SQLiteDatabase db = null;



    public ServerUdpateService() {
        super("update server");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
//        try {
//            initVars(intent);
//            if (action == ADD || action == UPDATE)
//                getLastEditionOnServer();
//            else if (action == DELETE)
//                informDeletion();
//            else
//                throw new NoSuchFieldException("le service n'a pas reçu une action valide à effecuter");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }

    private void initVars(Intent intent) throws NoSuchFieldException {
        dao = DAO.getInstance(getApplicationContext());
        db = dao.open();
        URL_API = getApplicationContext().getString(R.string.URL_API);
        URL_API_DEV = getApplicationContext().getString(R.string.URL_API_dev);
        intent.getExtras();
        if (intent.hasExtra(EXTRA_ID_USER))
        {
            System.out.println("l'user id a bien été envoyé au service");
            idUser = intent.getStringExtra(EXTRA_ID_USER);
        }
        else
           throw new NoSuchFieldException("le service n'as pas reçu l'id de l'utilisateur");
        if (intent.hasExtra(EXTRA_ACTION)) {
            action = intent.getShortExtra(EXTRA_ACTION, (short) 0);
            if (action == DELETE)
            {
                if (intent.hasExtra(EXTRA_ID_ENTRY))
                    idEntry = intent.getIntExtra(EXTRA_ID_ENTRY, 0);
                else
                    throw new NoSuchFieldException("le service n'as pas reçu l'id de l'entrée pour l'action de suppression");
            }
            //TODO CHANGER
            service = createServiceDev();
        }
        else
            throw new NoSuchFieldException("le service n'as pas reçu l'action a effectuer");

    }

//    public void getLastEditionOnServer()
//    {
//        System.out.println("le service c'est lancé et je vais récupérer les entrées du serveur");
//        ResponseCDSGetLastEdition responseCDS = null;
//        call = service.getLastEdition(idUser);
//        try {
//            Response<ResponseBody> reponse = call.execute();
//            if (reponse.isSuccess()) {
//                String body = reponse.body().string();
//                responseCDS = new ResponseCDSGetLastEdition(JsonUtils.getObj(body));
//                if (responseCDS.getError() == Carnetdesuivi.Error.NONE) {
//                    compareDates(responseCDS.getLastEdition());
//                }
//                else
//                    Log.e("getLastEditionOnServer", "erreur lors de la reception de la dateEdition");
//            } else
//                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void informDeletion() {
        call = service.deleteEntry(String.valueOf(idEntry), idUser);
        try {
            Response<ResponseBody> reponse = call.execute();
            if (reponse.isSuccess()) {
                String body = reponse.body().string();
                System.out.println("body sendMissingEntries = " + body);
            } else
                Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private ApiServices createService() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL_API)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        return (retrofit.create(ApiServices.class));
//    }

    private ApiServices createServiceDev() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API) //DEV BEFORE
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return (retrofit.create(ApiServices.class));
    }

//    private void compareDates(Date lastEditionServer) {
//        String lastEditionLocalStr = EntryOfCDSDAO.getLastEdition(idUser, db);
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATEEDITION_DB_FORMAT);
//            Date lastEditionLocal = sdf.parse(lastEditionLocalStr);
//            sdf.applyPattern(DateUtils.DATECREATION_DB_FORMAT);
//            lastEditionLocalStr = sdf.format(lastEditionLocal);
//            if (lastEditionServer != null)
//            {
//                String lastEdtitionServerStr = sdf.format(lastEditionServer);
//                if (lastEditionServer.before(lastEditionLocal)){
//                    System.out.println("let's go envoyer les entrées manquantes");
//                    sendMissingEntries(lastEdtitionServerStr, lastEditionLocalStr);
//                }
//            }
//            else{
//                sendMissingEntries(null, lastEditionLocal.toString());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    public void sendMissingEntries(String from, String to)
    {
        ArrayList<EntryToSend> missingEntries = new ArrayList<>();
        if (from != null) {
            System.out.println("il y a quelques entrées manquantes");
            missingEntries = EntryOfCDSDAO.selectBetweenDaysToSend(from, to, idUser, db);
        }
        else{
            System.out.println("Toutes les entrées sont manquantes !");
            missingEntries = EntryOfCDSDAO.selectAllToSend(idUser, db);
        }

        System.out.println("nombre d'entrées à envoyer = " + missingEntries.size());
        if (missingEntries.size() > 0)
        {
            ResponseCDSetMissingEntries responseCDS = null;
            service = createServiceDev();
            call = service.setMissingEntries(idUser, missingEntries);
            try {
                Response<ResponseBody> reponse = call.execute();
                if (reponse.isSuccess()) {
                    String body = reponse.body().string();
                    System.out.println("body sendMissingEntries = " + body);
                    responseCDS = new ResponseCDSetMissingEntries();
                } else
                    Log.e(getClass().getSimpleName(), "la requète est un echec. Code d'erreur : " + reponse.code() + "\n message d'erreur = " + reponse.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
