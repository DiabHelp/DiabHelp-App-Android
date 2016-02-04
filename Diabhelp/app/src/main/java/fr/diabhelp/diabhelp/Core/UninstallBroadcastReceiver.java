package fr.diabhelp.diabhelp.Core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by antoine on 26/01/2016.
 */
public class UninstallBroadcastReceiver extends BroadcastReceiver {

    private final Handler _handler;
    private final ParametresRecyclerAdapter _adapter;

    UninstallBroadcastReceiver(Handler handler, ParametresRecyclerAdapter adapter)
    {
        _handler = handler;
        _adapter = adapter;
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
      _handler.post(new Runnable() {
          @Override
          public void run() {
              String appname = intent.getDataString();
              appname = appname.substring(appname.indexOf(':') + 1);
              if (appname.contains("diabhelp"))
              {
                  _adapter.onModuleListUpdated(appname);
              }}
      });

    }
}
