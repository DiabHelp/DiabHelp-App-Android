package fr.diabhelp.medecin_patient.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sundava on 19/10/2016.
 */
public interface ApiService {

    @GET("patient/getChatMessages/{idUser}")
    Call<ResponseBody> getChatMessages(@Path("idUser") String userId, @Query("number")int number, @Query("timestamp") long timestamp);

    @FormUrlEncoded
    @POST("patient/postChatMessage")
    Call<ResponseBody> postChatMessage(@Field("user_id") String userId, @Field("message") String message, @Field("timestamp") long timestamp);

    @GET("patient/getAllRequestsByUserId/{idUser}")
    Call<ResponseBody> getPendingRequests(@Path("idUser") String userId);

    @GET("patient/getAllMedecinsByUserId/{idUser}")
    Call<ResponseBody> getMedecins(@Path("idUser") String userId);


    @FormUrlEncoded
    @POST("patient/manageRequest")
    Call<ResponseBody> acceptRequest(@Field("user_id") String userId, @Field("request_id") int requestId, @Field("status") int status);

    @FormUrlEncoded
    @POST("patient/manageRequest")
    Call<ResponseBody> denyRequest(@Field("user_id") String userId, @Field("request_id") int requestId, @Field("status") int status);


    @FormUrlEncoded
    @POST("patient/deleteMedecin")
    Call<ResponseBody> deleteMedecin(@Field("user_id") String userId, @Field("medecin_id") int medecinId);

}
