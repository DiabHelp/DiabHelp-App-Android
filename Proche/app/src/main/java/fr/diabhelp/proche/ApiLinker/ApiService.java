package fr.diabhelp.proche.ApiLinker;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Sumbers on 25/07/2016.
 */
public interface ApiService {

//    @FormUrlEncoded
//    @POST("/user/add")
//    Call<ResponseRegister> addUser(@Field("mail") String mail, @Field("phone") String phone, @Field("pwd")String pwd, @Field("firstname") String firstname, @Field("lastname") String lastname, @Field("codePromo") String codePromo, @FieldMap(encoded = true) Map<String, String> bIds);


    @GET("proche/getAllByUserId/{idUser}")
    Call<ResponseBody> getPendingRequests(@Path("idUser") String userId);

    @GET("proche/search/{search}")
    Call<ResponseBody> searchPatient(@Path("search") String query);


    @FormUrlEncoded
    @POST("proche/manageProcheList")
    Call<ResponseBody> sendDemande(@Field("id_proche") String idProche, @Field("id_patient") String idPatient, @Field("status") int status);

    @FormUrlEncoded
    @POST("proche/manageProcheList")
    Call<ResponseBody> acceptRequest(@Field("id_proche") String idProche, @Field("id_patient") String idPatient, @Field("status") int status);


    @FormUrlEncoded
    @POST("proche/manageProcheList")
    Call<ResponseBody> denyRequest(@Field("id_proche") String idProche, @Field("id_patient") String idPatient, @Field("status") int status);

}
