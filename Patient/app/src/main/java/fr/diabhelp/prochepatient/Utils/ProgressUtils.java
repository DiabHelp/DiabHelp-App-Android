package fr.diabhelp.proche.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Sumbers on 26/07/2016.
 */
public class ProgressUtils {

    private Context context;

    public ProgressUtils(Context context) {
        this.context = context;
    }

    public ProgressDialog make(String message, int style, boolean cancelable)
    {
        ProgressDialog progress;
        progress = new ProgressDialog(context);
        progress.setCancelable(cancelable);
        progress.setMessage(message);
        progress.setProgressStyle(style);
        return (progress);
    }

//    public ProgressDialog make()


}
