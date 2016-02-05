package fr.diabhelp.diabhelp.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
}
