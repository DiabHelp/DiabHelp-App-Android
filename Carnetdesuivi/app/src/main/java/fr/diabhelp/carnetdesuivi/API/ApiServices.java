package fr.diabhelp.carnetdesuivi.API;

import java.util.ArrayList;

import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryToSend;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by Sumbers on 29/03/2016.
 */
public interface ApiServices {

    @Multipart
    @POST("/api/carnet/exportJSON")
    Call<ResponseBody> sendEmail(@Part("id_user") String idUser, @Part("email") String mail, @PartMap ArrayList<EntryToSend> datas);

        @GET("/api/carnet/entry/getAllByUserId/{idUser}")
    Call<ResponseBody> getAllEntries(@Path("idUser") String idUser);

    @GET("/api/carnet/entry/getLastDate/{idUser}")
    Call<ResponseBody> getLastEdition(@Path("idUser") String idUser);

    @POST("/api/carnet/entry/setFromApp/{idUser}")
    Call<ResponseBody> setMissingEntries(@Path("idUser") String idUser, @Body ArrayList<EntryToSend> entries);

    @DELETE("/carnet/entry/delete/{id}/{idUser}")
    Call<ResponseBody> deleteEntry(@Path("id") String idEntry, @Path("idUser") String idUser);
}
