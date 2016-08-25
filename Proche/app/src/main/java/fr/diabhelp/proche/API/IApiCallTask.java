package fr.diabhelp.proche.API;

import org.json.JSONException;

/**
 * Created by 4kito on 02/08/2016.
 */
public interface IApiCallTask<T> {
    void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException;

    /**
     * Fonction appelée dans la fonction {@link AsyncTask#onPostExecute(Object)}des classes héritants de {@link AsyncTask}
     * pour récupérer le résultat d'une requète à l'API.
     * @param bodyResponse correspond à l'objet spécifique retourné par un appel à l'API
     * @param action correspond à l'action à effectuée avec le @param bodyResponse
     * @param progress correspond au dialogue de chargement initié dans la fonction {@link AsyncTask#onPreExecute()} des classes héritants de {@link AsyncTask}
     */
}
