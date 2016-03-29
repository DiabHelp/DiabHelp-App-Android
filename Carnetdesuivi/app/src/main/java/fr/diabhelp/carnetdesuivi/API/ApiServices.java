package fr.diabhelp.carnetdesuivi.API;

import java.util.ArrayList;

import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Sumbers on 29/03/2016.
 */
public interface ApiServices {

    @FormUrlEncoded
    @POST
    Call<ResponseBody> sendEmail(@Field("token") String token, @Field("datas") ArrayList<EntryOfCDS> datas);
}
