package fr.diabhelp.carnetdesuivi.API.Response;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public ResponseCDSGetAllEntries(JSONObject datas)
    {
        try {
            if (datas != null)
            {
                Boolean success = JsonUtils.getBoolFromKey(datas, "success");
                if (success != null)
                {
                    if (success == true) {
                        entries = new ArrayList<EntryOfCDS>();
                        JSONArray arr = JsonUtils.getArray(datas, "0");
                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonEntry = JsonUtils.getObjFromArray(arr, i);
                                EntryOfCDS entry = new EntryOfCDS();
                                entry.setIdUser(jsonEntry.getString("idUser"));
                                entry.setActivity(jsonEntry.getString("activity"));
                                entry.setActivityType(jsonEntry.getString("activityType"));
                                entry.setAlcohol(jsonEntry.getInt("alcohol"));
                                entry.setAthome(jsonEntry.getInt("athome"));
                                entry.setAtwork(jsonEntry.getInt("work"));
                                entry.setBreakfast(jsonEntry.getInt("breakfast"));
                                entry.setDate(jsonEntry.getString("date"));
                                entry.setTitle(jsonEntry.getString("title"));
                                entry.setPlace(jsonEntry.getString("place"));
                                entry.setGlucide(jsonEntry.getDouble("glucide"));
                                entry.setNotes(jsonEntry.getString("notes"));
                                entry.setFast_insu(jsonEntry.getDouble("fastInsu"));
                                entry.setSlow_insu(jsonEntry.getDouble("slowInsu"));
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
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("MM-dd-yyyy");
                                SimpleDateFormat simpleFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Long date = jsonEntry.getLong("date");
                                System.out.println("timestamp reçu = " + date);
                                Date d = new Date(date * 1000L);
                                entry.setDate(simpleFormat.format(d));
                                Long dateEdition = jsonEntry.getLong("dateEdition");
                                Date dEdition = new Date(dateEdition * 1000L);
                                entry.setDateEdition(simpleFormat2.format(dEdition));
                                entries.add(entry);
                            }
                        }
                    }
                }
                else
                {
                    Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
                    error = Carnetdesuivi.Error.SERVER_ERROR;
                }
            }
            else
            {
                Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
                error = Carnetdesuivi.Error.SERVER_ERROR;
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
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
