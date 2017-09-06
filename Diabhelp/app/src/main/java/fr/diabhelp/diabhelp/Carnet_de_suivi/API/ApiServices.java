package fr.diabhelp.diabhelp.Carnet_de_suivi.API;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.Ressource.EntryToSend;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sumbers on 29/03/2016.
 */
public interface ApiServices {

    @POST("/api/carnet/exportJSON")
    Call<ResponseBody> sendEmail(@Query("id_user") String idUser, @Query("email") String mail, @Body ArrayList<EntryToSend> datas);

        @GET("/api/carnet/entry/getAllByUserId/{idUser}")
    Call<ResponseBody> getAllEntries(@Path("idUser") String idUser);

    @GET("/api/carnet/entry/getLastDate/{idUser}")
    Call<ResponseBody> getLastEdition(@Path("idUser") String idUser);

    @POST("/api/carnet/entry/setFromApp/{idUser}")
    Call<ResponseBody> setMissingEntries(@Path("idUser") String idUser, @Body ArrayList<EntryToSend> entries);

    @DELETE("/api/carnet/entry/delete/{id}/{idUser}")
    Call<ResponseBody> deleteEntry(@Path("id") String idEntry, @Path("idUser") String idUser);
}
