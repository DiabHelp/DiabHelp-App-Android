package fr.diabhelp.proche.API;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4kito on 02/08/2016.
 */
public class ApiCallTask extends AsyncTask<String, Integer, String> {
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int DELETE = 2;
    public static final int ARRAY = 0;
    public static final int OBJECT = 1;
    //    private final String URL_API = "http://naquedounet.fr/api/";
    private final String URL_API = "http://sundava.com:8000/medecin_patient/";

    private IApiCallTask _obj = null;
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
        HttpClient httpclient = new DefaultHttpClient();
        try
        {
            int nb;

            nb = Integer.parseInt(params[0]);
            HttpResponse httpresponse;
            switch (this.request)
            {
                case GET:
                    httpresponse = doGet(nb, httpclient, params);
                    break;
                case POST:
                    httpresponse = doPost(nb, httpclient, params);
                    break;
                case DELETE:
                    httpresponse = doDelete(nb, httpclient, params);
                    break;
                default:
                    return "";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));
            String s;
            String response = null;
            response = reader.readLine();
            while ((s = reader.readLine()) != null)
                response += s;
            return (response);
        }
        catch (URISyntaxException e) {
            return ("ERROR : Syntax Problem");
        }
        catch (ClientProtocolException e) {
            return ("ERROR : Protocol Exception " + e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            return ("ERROR : IO Exception");
        }
    }

    protected HttpResponse doPost(int nb, HttpClient httpclient, String... params) throws UnsupportedEncodingException, IOException, URISyntaxException
    {
        Log.d("APICallTask", "POST url + " + URL_API + params[1]);
        URI serv = new URI(URL_API + params[1]);
        HttpPost httppost = new HttpPost(serv);

        int max = 2 + nb * 2;
        int i = 2;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(nb);
        while (i < max)
        {
            nameValuePairs.add(new BasicNameValuePair(params[i], params[i + 1]));
            i = i + 2;
        }
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        return (httpclient.execute(httppost));
    }

    protected HttpResponse doGet(int nb, HttpClient httpclient, String... params) throws UnsupportedEncodingException, IOException, URISyntaxException
    {
        Log.d("APICallTask", "GET url + " + URL_API + params[1]);
        String url = URL_API + params[1];
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
        HttpGet httppost = new HttpGet(serv);
        return (httpclient.execute(httppost));
    }

    protected HttpResponse doDelete(int nb, HttpClient httpclient, String... params) throws UnsupportedEncodingException, IOException, URISyntaxException
    {
        Log.d("APICallTask", "DELETE url + " + URL_API + params[1]);
        String url = URL_API + params[1];
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
        HttpDelete httppost = new HttpDelete(serv);
        return (httpclient.execute(httppost));
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
