package fr.diabhelp.carnetdesuivi;

import android.app.ProgressDialog;

/**
 * Created by Sumbers on 29/03/2016.
 */
public interface IApiCallTask<T> {
    void onBackgroundTaskCompleted(T response, String action, ProgressDialog progress);
}
