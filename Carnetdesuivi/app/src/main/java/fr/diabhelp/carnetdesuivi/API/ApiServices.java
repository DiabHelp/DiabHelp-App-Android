package fr.diabhelp.carnetdesuivi.API;

import java.util.ArrayList;

import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Sumbers on 29/03/2016.
 */
public interface ApiServices {

    @FormUrlEncoded
    @POST
    Call<ResponseBody> sendEmail(@Field("token") String token, @Field("datas") ArrayList<EntryOfCDS> datas);

    @GET("app_dev.php/api/cds/entry/getAllByUserId")
    Call<ResponseBody> getAllEntries(@Query("idUser") String idUser);

    @GET("app_dev.php/api/cds/entry/getLastDate")
    Call<ResponseBody> getLastEdition(@Query("idUser") String idUser);

    @POST("app_dev.php/api/cds/entry/setFromApp")
    Call<ResponseBody> setMissingEntries(@Field("idUser") String idUser, @Body ArrayList<EntryOfCDS> entries);
}
