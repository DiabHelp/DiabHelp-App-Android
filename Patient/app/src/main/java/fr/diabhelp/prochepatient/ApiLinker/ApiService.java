package fr.diabhelp.prochepatient.ApiLinker;

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


    @GET("user/getInfo/{idUser}")
    Call<ResponseBody>  getUserInfo(@Path("idUser") String id);

    @GET("patient/getAllByUserId/{id_user}")
    Call<ResponseBody> getProchePendingRequests(@Path("id_user") String userId);
    @GET("proche/getAllByUserId/{id_user}")
    Call<ResponseBody> getPatientPendingRequests(@Path("id_user") String userId);

    @GET("patient/getAllByUserId/{id_user}")
    Call<ResponseBody> getAllProches(@Path("id_user") String idUser);
    @GET("proche/getAllByUserId/{id_user}")
    Call<ResponseBody> getAllPatients(@Path("id_user") String idUser);

    @GET("patient/search/{id_user}/{search}")
    Call<ResponseBody> searchProche(@Path("id_user") String idUser, @Path("search") String query);
    @GET("proche/search/{id_user}/{search}")
    Call<ResponseBody> searchPatient(@Path("id_user") String idUser, @Path("search") String query);


    @FormUrlEncoded
    @POST("patient/manageProcheList")
    Call<ResponseBody> sendDemandeToProche(@Field("id_proche") String idProche, @Field("id_patient") String idPatient, @Field("status") int status);
    @FormUrlEncoded
    @POST("proche/manageProcheList")
    Call<ResponseBody> sendDemandeToPatient(@Field("id_patient") String idPatient, @Field("id_proche") String idProche, @Field("status") int status);


    @FormUrlEncoded
    @POST("patient/manageProcheList")
    Call<ResponseBody> acceptProcheRequest(@Field("id_proche") String idProche, @Field("id_patient") String idPatient, @Field("status") int status);
    @FormUrlEncoded
    @POST("proche/managePatientList")
    Call<ResponseBody> acceptPatientRequest(@Field("id_patient") String idProche, @Field("id_proche") String idPatient, @Field("status") int status);

    @FormUrlEncoded
    @POST("patient/manageProcheList")
    Call<ResponseBody> denyProcheRequest(@Field("id_proche") String idProche, @Field("id_patient") String idPatient, @Field("status") int status);
    @FormUrlEncoded
    @POST("proche/managePatientList")
    Call<ResponseBody> denyPatientRequest(@Field("id_patient") String idProche, @Field("id_proche") String idPatient, @Field("status") int status);

    @FormUrlEncoded
    @POST("patient/{id_patient}/alert/{id_proche}")
    Call<ResponseBody> sendAlertToSpecificUser(@Path("id_patient") String idPatient, @Path("id_proche") String idProche, @Field("date") String date, @Field("message") String message);

    @FormUrlEncoded
    @POST("patient/{id_patient}/alert")
    Call<ResponseBody> sendAlertToAllContacts(@Path("id_patient") String idPatient, @Field("date") String date, @Field("message") String message);
    @FormUrlEncoded
    @POST("patient/setPatientPosition")
    Call<ResponseBody> sendPatientPosition(@Field("id_user") String idUser, @Field("position") String position);
}
