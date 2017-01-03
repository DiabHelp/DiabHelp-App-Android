package fr.diabhelp.diabhelp.API;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sumbers on 28/01/2016.
 */

/**
 * Interface for retrofit 2.0 which allow to make HTTP requests
 */
public interface ApiServices {

        @GET("/api/user/getInfo/{idUser}")
        Call<ResponseBody>  getInfo(@Path("idUser") String id);

        @GET("/api/modules/all")
        Call<ResponseBody> getModules();
        //TODO
//        Call<ResponseBody> getModules(@Query("typeUser") JSONArray typeUser);

        @FormUrlEncoded
        @POST("/api/user/setInfo")
        Call<ResponseBody>  setInfo(@Field("id") String id, @Field("email") String email, @Field("firstname") String firstname, @Field("lastname") String lastname, @Field("phone") String phone, @Field("birthdate") String birthdate, @Field("organisme") String organisme, @Field("password") String password);


        @FormUrlEncoded
        @POST("/logout")
        Call<ResponseBody> logout(@Field("token") String token);

        @FormUrlEncoded
        @POST("/login_check")
        Call<ResponseBody> getBasicAuthSession(@Field("username") String usr, @Field("password") String pwd);

        @FormUrlEncoded
        @POST("/rest-login")
        Call<ResponseBody> getAuth(@Field("username") String usr, @Field("password") String pwd);

        @FormUrlEncoded
        @POST("/api/user/register")
        Call<ResponseBody> register(@Field("username") String usr,@Field("email") String mail, @Field("password") String pwd, @Field("role") String role, @Field("firstname") String first, @Field("lastname") String last);

        @FormUrlEncoded
        @POST("/api/user/setFCMToken")
        Call<ResponseBody> sentTokenBindWithUser(@Field("id_user") String idUser, @Field("token") String token);

}
