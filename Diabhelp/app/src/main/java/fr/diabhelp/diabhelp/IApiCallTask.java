package fr.diabhelp.diabhelp;

import org.json.JSONException;

/**
 * Created by Sumbers on 10/10/2015.
 */
public interface IApiCallTask {
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException;
}
