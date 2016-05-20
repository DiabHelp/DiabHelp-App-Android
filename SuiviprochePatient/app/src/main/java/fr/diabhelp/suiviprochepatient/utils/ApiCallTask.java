package fr.diabhelp.suiviprochepatient.utils;


import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ApiCallTask extends AsyncTask<String, Integer, String> {
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int DELETE = 2;
    public static final int ARRAY = 0;
    public static final int OBJECT = 1;
    private final String URL_API = "http://naquedounet.fr/api/";

    IApiCallTask _obj = null;
    private int request;
    private int type;
    private String _action = null;

     public ApiCallTask(IApiCallTask obj, int req, int typ, String act)
    {
        this._obj = obj;
        this.request = req;
        this.type = typ;
        this._action = act;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try
        {
            int nb;

            nb = Integer.parseInt(params[0]);
            switch (this.request)
            {
                case GET:
                  doGet(nb, params);
                    break;
                case POST:
                  doPost(nb,params);
                    break;
                case DELETE:
                   doDelete(nb, params);
                    break;
                default:
                    return "";
            }
            BufferedReader reader = null;
            String s;
            String response = null;
            response = reader.readLine();
            while ((s = reader.readLine()) != null)
                response += s;
            System.out.println("page accueil retour telechargement readerlaca : " + response);
            return (response);
        }
        catch (URISyntaxException e) {
            return ("syntax problem");
        }
        catch (IOException e) {
            return ("io exception");
        }
    }

    protected void doPost(int nb, String... params) throws UnsupportedEncodingException, IOException, URISyntaxException
    {
        System.out.println("url + " + URL_API + params[1] + ".php");
        URI serv = new URI(URL_API + params[1] + ".php");

        int max = 2 + nb * 2;
        int i = 2;
        while (i < max)
        {
            i = i + 2;
        }
        return;
    }

    protected void doGet(int nb, String... params) throws UnsupportedEncodingException, IOException, URISyntaxException
    {
        String url = URL_API + params[1] + ".php";
        if (nb > 0)
        {
            url += "?" + params[2] + "=" + params[3];
        }
        int max = 2 + nb * 2;
        int i = 4;

        while (i < max)
        {
            url += "&" + params[i] + "=" + params[i + 1];
            i = i + 2;
        }
        URI serv = new URI(url);
        return;
    }

    protected void doDelete(int nb, String... params) throws UnsupportedEncodingException, IOException, URISyntaxException
    {
        String url = URL_API + params[1] + ".php";
        if (nb > 0)
        {
            url += "?" + params[2] + "=" + params[3];
        }
        int max = 2 + nb * 2;
        int i = 4;

        while (i < max)
        {
            url += "&" + params[i] + "=" + params[i + 1];
            i = i + 2;
        }
        URI serv = new URI(url);
        return;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            _obj.onBackgroundTaskCompleted(s, this.type, this._action);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
