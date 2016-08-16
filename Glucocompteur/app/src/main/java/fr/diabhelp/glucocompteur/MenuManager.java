package fr.diabhelp.glucocompteur;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoine on 26/01/2016.
 */
public class MenuManager {

    private ArrayList<Menu> savedMenuList = new ArrayList<>();

    private File savedFile;
    MenuManager(String filename)
    {
        savedFile = new File(filename);
        try {
            if (savedFile.exists()) {
                loadMenu();
            }
        } catch (IOException e) {
            Log.d("MenuFavori", "No JSON file found");
           // e.printStackTrace();
        }
    }

    public ArrayList<Menu> getSavedMenu()
    {
        return  savedMenuList;
    }

    public void loadMenu() throws IOException {
        FileReader fileReader = new FileReader(savedFile);
        BufferedReader in = new BufferedReader(fileReader);
        String currentContact = in.readLine();
        StringBuilder sb = new StringBuilder();
        while(currentContact != null) {
            sb.append(currentContact);
            sb.append(System.getProperty("line.separator"));
            currentContact = in.readLine();
        }
        Log.d("MenuManager", String.valueOf(sb));
        savedMenuList = new ArrayList<>();
        Menu savedMenu;
        JsonReader reader = new JsonReader(new FileReader(savedFile));
        try {
            if (reader.hasNext()) {
                reader.beginArray(); // Begin Menu Array
                while (reader.hasNext()) {
                    savedMenu = new Menu();
                    reader.beginObject(); // Begin menuInfo object
                    if (reader.nextName().equals("menuName"))
                        savedMenu.setMenuName(reader.nextString());
                    if (reader.nextName().equals("menuGlucids"))
                        savedMenu.setMenuGlucids(reader.nextDouble());
                    reader.endObject(); // End menuInfo object
                    reader.beginArray(); //Begin Aliment Array
                    //Log.d("HELLO", savedMenu.menuName + " " + savedMenu.menuGlucids);
                    while (reader.hasNext())
                        savedMenu.addAliment(readAliment(reader));
                    reader.endArray(); // End Aliment Array
                    savedMenuList.add(savedMenu);
                }
                reader.endArray(); //End Menu Array
            }
        }
        finally {
            reader.close();
        }
        Log.d("MenuFavori", "JSON file found");

    }

    private Aliment readAliment(JsonReader reader) throws IOException {
        Log.d("MenuFavori", "Reading Aliment...");
        String name = null;
        double weight = 1.0f;
        double glucids = 1.0f;
        double totalGlucids = 1.0f;
        reader.beginObject();
        while (reader.hasNext())
        {
            switch (reader.nextName()) {
                case ("name"):
                    name = reader.nextString();
                    break;
                case ("weight"):
                    weight = Double.parseDouble(String.valueOf(reader.nextDouble()));
                    break;
                case ("glucides"):
                    glucids = Double.parseDouble(String.valueOf(reader.nextDouble()));
                    break;
                case ("totalGlucides"):
                    totalGlucids = Double.parseDouble(String.valueOf(reader.nextDouble()));
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        Log.d("MenuFavori", "Aliment attributes : name=" + name + ", weight=" + weight + ", glucids=" + glucids + ", totalGlucids=" + totalGlucids);
        return new Aliment(name, weight, glucids);
    }

    public void clearMenu() {
        Log.d("MenuFavori", "Deleting menus_favoris.json");

        savedFile.delete();
    }

    public void debug() {
        Log.d("MenuFavori", "Debug");

        for (Menu savedMenu : savedMenuList) {
            Log.d("MenuFavori", "------- Begin Menu -------");
            Log.d("MenuFavori", "    ------- Begin Info ---");
            Log.d("MenuFavori", "Name : " + savedMenu.getMenuName());
            Log.d("MenuFavori", "Glucides : " + savedMenu.getMenuGlucids());
            Log.d("MenuFavori", "    ------- End Info ---");
            for (Aliment aliment : savedMenu.getAlimentsList()) {
                Log.d("MenuFavori", aliment.toString());
            }
            Log.d("MenuFavori", "------- End Menu -------");

        }
    }
}
