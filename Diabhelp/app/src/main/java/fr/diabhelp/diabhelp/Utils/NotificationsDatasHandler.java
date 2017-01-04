package fr.diabhelp.diabhelp.Utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.Map;

/**
 * Created by Sumbers on 04/01/2017.
 */

public class NotificationsDatasHandler {

    public static void handle(Map<String, String> datas, Context context) {
        if (datas.containsKey("PNAME") && datas.containsKey("APPNAME"))
        {
            NotificationsDatasHandler.handleLaunchModule(datas, context);
        }
    }

    private static void handleLaunchModule(Map<String, String> datas, Context context)
    {
        String unflat = datas.get("PNAME");
        Intent intent = new Intent(unflat);
        String appnameclean;

        appnameclean =datas.get("APPNAME");
        appnameclean = appnameclean.replaceAll("\\s", "");

        unflat = unflat.concat("/").concat(datas.get("PNAME")).concat(".").concat(appnameclean);

        intent.setComponent(ComponentName
                .unflattenFromString(unflat));
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.putExtra("id_user", datas.get("ID_USER"));

        if (datas.containsKey("EVENT"))
        {
            switch (datas.get("EVENT"))
            {
                case  "alert_patient":
                {
                    intent = NotificationsDatasHandler.handleGetAlert(datas, intent);
                    break;
                }
            }
        }
        context.startActivity(intent);
    }

    private static Intent handleGetAlert(Map<String, String> datas, Intent intent)
    {
        intent.putExtra("id_patient", datas.get("ID_PATIENT"));
        intent.putExtra("nom", datas.get("NAME_USER"));
        intent.putExtra("message", datas.get("MESSAGE"));
        intent.putExtra("position", datas.get("POSITION"));
        intent.putExtra("carnet", datas.get("CARNET_SUIVI"));
        return (intent);
    }
}
