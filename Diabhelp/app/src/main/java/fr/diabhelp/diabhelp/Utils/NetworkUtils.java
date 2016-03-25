package fr.diabhelp.diabhelp.Utils;

/**
 * Created by naqued on 10/11/15.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatusType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static Boolean getConnectivityStatus(Context context) {
        int conn = NetworkUtils.getConnectivityStatusType(context);
        Boolean status = null;

        if (conn == NetworkUtils.TYPE_WIFI) {
            status = true;
        } else if (conn == NetworkUtils.TYPE_MOBILE) {
            status = true;
        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
            status = false;
        }
        return status;
    }
}
