package fr.diabhelp.carnetdesuivi.API.Response;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class ResponseCDSGetAllEntries {

    Carnetdesuivi.Error error = Carnetdesuivi.Error.NONE;

    List<EntryOfCDS> entries;

    public ResponseCDSGetAllEntries(){}

    public ResponseCDSGetAllEntries(JSONArray arr)
    {
        try {
            entries = new ArrayList<EntryOfCDS>();
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject jsonEntry = JsonUtils.getObjFromArray(arr, i);
                EntryOfCDS entry = new EntryOfCDS();
                entry.setActivity(jsonEntry.getString("activity"));
                entry.setActivityType(jsonEntry.getString("activity_type"));
                entry.setAlcohol(jsonEntry.getInt("alcohol"));
                entry.setAthome(jsonEntry.getInt("athome"));
                entry.setAtwork(jsonEntry.getInt("work"));
                entry.setBreakfast(jsonEntry.getInt("breakfast"));
                entry.setDate(jsonEntry.getString("date"));
                entry.setTitle(jsonEntry.getString("title"));
                entry.setPlace(jsonEntry.getString("place"));
                entry.setGlucide(jsonEntry.getDouble("glucide"));
                entry.setNotes(jsonEntry.getString("notes"));
                entry.setFast_insu(jsonEntry.getDouble("fast_insu"));
                entry.setSlow_insu(jsonEntry.getDouble("slow_insu"));
                entry.setHba1c(jsonEntry.getDouble("hba1c"));
                entry.setHour(jsonEntry.getString("hour"));
                entry.setglycemy(jsonEntry.getDouble("glycemy"));
                entry.setLaunch(jsonEntry.getInt("lunch"));
                entry.setDiner(jsonEntry.getInt("diner"));
                entry.setEncas(jsonEntry.getInt("encas"));
                entry.setSleep(jsonEntry.getInt("sleep"));
                entry.setWakeup(jsonEntry.getInt("wakeup"));
                entry.setNight(jsonEntry.getInt("night"));
                entry.setWorkout(jsonEntry.getInt("workout"));
                entry.setHypogly(jsonEntry.getInt("hypogly"));
                entry.setHypergly(jsonEntry.getInt("hypergly"));
                entry.setPeriod(jsonEntry.getInt("period"));
                entry.setDateEdition(jsonEntry.getString("date_edition"));
                entries.add(entry);
            }

        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + arr.toString() + "]");
            error = Carnetdesuivi.Error.SERVER_ERROR;

        }
    }

    public List<EntryOfCDS> getEntries() {
        return this.entries;
    }

    public Carnetdesuivi.Error getError() {
        return this.error;
    }

    public void setError(Carnetdesuivi.Error error) {
        this.error = error;
    }
}
