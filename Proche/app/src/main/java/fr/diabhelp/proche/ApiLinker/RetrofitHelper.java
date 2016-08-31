package fr.diabhelp.proche.ApiLinker;

import android.content.Context;

import fr.diabhelp.proche.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sumbers on 26/07/2016.
 */
public class RetrofitHelper {

    private String urlApiDEV;
    private String urlApiPROD;
    private Context context;

    public RetrofitHelper(Context context){
        this.context = context;
        this.urlApiPROD = this.context.getString(R.string.api_url_prod);
        this.urlApiDEV = this.context.getString(R.string.api_url_dev);
    }

    public ApiService createService(Build build)
    {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl((build == Build.PROD) ? this.urlApiPROD : this.urlApiDEV)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return (retrofit.create(ApiService.class));
    }

    public enum Build{
        PROD,
        DEV
    }
}
