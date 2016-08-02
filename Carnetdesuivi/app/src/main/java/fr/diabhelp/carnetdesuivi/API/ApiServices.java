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
import retrofit2.http.Path;

/**
 * Created by Sumbers on 29/03/2016.
 */
public interface ApiServices {

    @FormUrlEncoded
    @POST
    Call<ResponseBody> sendEmail(@Field("token") String token, @Field("datas") ArrayList<EntryOfCDS> datas);

        @GET("/api/carnet/entry/getAllByUserId/{idUser}")
    Call<ResponseBody> getAllEntries(@Path("idUser") String idUser);

    @GET("/api/carnet/entry/getLastDate/{idUser}")
    Call<ResponseBody> getLastEdition(@Path("idUser") String idUser);

    @POST("/api/carnet/entry/setFromApp")
    Call<ResponseBody> setMissingEntries(@Body ArrayList<EntryOfCDS> entries);
}
