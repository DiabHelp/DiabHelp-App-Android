package fr.diabhelp.glucocompteur;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by antoine on 01/02/2016.
 */
public class JSONMenuWriter {

    private final File savedFile;

    JSONMenuWriter(String filename)
    {
        savedFile = new File(filename);
    }

    public void saveMenu(ArrayList<Aliment> menu) {
        Log.d("MenuFavori", "Saving menus_favoris.json");

        JSONArray JSONMenu = new JSONArray();
        JSONObject JSONAliment = null;
        try {
            for (Aliment aliment : menu) {
                JSONAliment = new JSONObject();
                JSONAliment.put("name", aliment.getName());
                JSONAliment.put("weight", aliment.getWeight());
                JSONAliment.put("glucides", aliment.getGlucids());
                JSONAliment.put("totalGlucides", aliment.getTotalGlucids());
                JSONMenu.put(JSONAliment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!savedFile.exists())
                savedFile.createNewFile();
            Writer writer = new BufferedWriter(new FileWriter(savedFile));
            writer.write(JSONMenu.toString());
            writer.close();
        } catch (IOException e) {
            Log.d("MenuFavori", "Could not create file writer");
            e.printStackTrace();
        }
    }
}
