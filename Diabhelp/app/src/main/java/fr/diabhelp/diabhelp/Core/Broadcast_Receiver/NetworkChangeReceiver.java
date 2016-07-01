package fr.diabhelp.diabhelp.Core.Broadcast_Receiver;

/**
 * Created by naqued on 10/11/15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.Utils.NetworkUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Boolean status = NetworkUtils.getConnectivityStatus(context);
        CoreActivity activity = (CoreActivity) context;
        activity.setNetStateAndChange(status);
        Toast.makeText(context, status.toString(), Toast.LENGTH_LONG).show();
    }
}