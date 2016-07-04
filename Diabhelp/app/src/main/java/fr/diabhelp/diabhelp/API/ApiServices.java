package fr.diabhelp.diabhelp.API;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Sumbers on 28/01/2016.
 */

/**
 * Interface for retrofit 2.0 which allow to make HTTP requests
 */
public interface ApiServices {
        @FormUrlEncoded
        @POST("app_dev.php/login_check")
        Call<ResponseBody> getBasicAuthSession(@Field("username") String usr, @Field("password") String pwd);

        @FormUrlEncoded
        @POST("app_dev.php/api/")
        Call<ResponseBody> getAuth(@Field("username") String usr, @Field("password") String pwd);

        @FormUrlEncoded
        @POST("app_dev.php/api/users/add")
        Call<ResponseBody> register(@Field("username") String usr,@Field("email") String mail, @Field("password") String pwd, @Field("role") String role, @Field("firstname") String first, @Field("lastname") String last);


        @FormUrlEncoded
        @POST("app_dev.php/logout")
        Call<ResponseBody> logout(@Field("token") String token);

        @GET("app_dev.php/api/modules/all")
        Call<ResponseBody> getModules();
        //TODO
//        Call<ResponseBody> getModules(@Query("typeUser") JSONArray typeUser);
}