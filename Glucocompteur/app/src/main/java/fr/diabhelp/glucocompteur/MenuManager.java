package fr.diabhelp.glucocompteur;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoine on 26/01/2016.
 */
public class MenuManager {

    private ArrayList<Aliment> savedMenu;

    private File savedFile;
    MenuManager(String filename)
    {
        savedFile = new File(filename);
        try {
            loadMenu();
        } catch (IOException e) {
            Log.d("MenuFavori", "No JSON file found");
            e.printStackTrace();
        }
    }

    public ArrayList<Aliment> getSavedMenu()
    {
        return  savedMenu;
    }

    public void loadMenu() throws IOException {
        savedMenu = new ArrayList<>();
        JsonReader reader = new JsonReader(new FileReader(savedFile));
        try {
            reader.beginArray();
            while (reader.hasNext())
                savedMenu.add(readAliment(reader));
            reader.endArray();
        }
        finally {
            reader.close();
        }
        Log.d("MenuFavori", "JSON file found");

    }

    private Aliment readAliment(JsonReader reader) throws IOException {
        Log.d("MenuFavori", "Reading Aliment...");
        String name = null;
        Float weight = -1.0f;
        Float glucids = -1.0f;
        Float totalGlucids = -1.0f;
        reader.beginObject();
        while (reader.hasNext())
        {
            switch (reader.nextName()) {
                case ("name"):
                    name = reader.nextString();
                    break;
                case ("weight"):
                    weight = Float.parseFloat(reader.nextString());
                    break;
                case ("glucids"):
                    glucids = Float.parseFloat(reader.nextString());
                    break;
                case ("totalGlucids"):
                    totalGlucids = Float.parseFloat(reader.nextString());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        Log.d("MenuFavori", "Aliment attributes : name=" + name + ", weight=" + weight + ", glucids=" + glucids + ", totalGlucids=" + totalGlucids);
        return new Aliment(name, weight, glucids, totalGlucids);
    }

    public void clearMenu() {
        Log.d("MenuFavori", "Deleting menus_favoris.json");

        savedFile.delete();
    }

    public void debug() {
        Log.d("MenuFavori", "Debug");

        for (Aliment aliment : savedMenu)
        {
            Log.d("MenuFavori", aliment.toString());
        }
    }
}
