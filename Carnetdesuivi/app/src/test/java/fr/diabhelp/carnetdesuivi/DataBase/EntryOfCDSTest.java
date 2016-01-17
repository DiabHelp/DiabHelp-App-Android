package fr.diabhelp.carnetdesuivi.DataBase;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sumbers on 17/01/2016.
 */
public class EntryOfCDSTest extends TestCase {

    //TODO test json
    public void testSerializationEntry() throws Exception{
        System.out.println("#test");

        List<EntryOfCDS> listEntries = new ArrayList<EntryOfCDS>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        String heure = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        String heureMinute = heure + ":" + minute;
        EntryOfCDS entry = new EntryOfCDS(dateFormat.format(cal.getTime()));
        entry.setNotes("Une note");
        entry.setTitle("Un titre");
        entry.setGlucide("13");
        entry.setActivity("une activité");
        entry.setActivityType("un type d'activité");
        entry.setFast_insu("0.02");
        entry.setSlow_insu("0.03");
        entry.setHba1c("2.00");
        entry.setHour(heureMinute);
        entry.setglycemy("32.4");
        entry.setBreakfast(0);
        entry.setLaunch(1);
        entry.setDiner(2);
        entry.setEncas(3);
        entry.setSleep(4);
        entry.setWakeup(5);
        entry.setNight(6);
        entry.setWorkout(7);
        entry.setHypogly(8);
        entry.setHypergly(9);
        entry.setAtwork(10);
        entry.setAthome(11);
        entry.setAlcohol(12);
        entry.setPeriod(13);
        listEntries.add(entry);

        cal = Calendar.getInstance();
        heure = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(cal.get(Calendar.MINUTE));
        heureMinute = heure + ":" + minute;
        EntryOfCDS entryDeux = new EntryOfCDS(dateFormat.format(cal.getTime()));
        entryDeux.setNotes("Une note");
        entryDeux.setTitle("Un titre");
        entryDeux.setGlucide("13");
        entryDeux.setActivity("une activité");
        entryDeux.setActivityType("un type d'activité");
        entryDeux.setFast_insu("0.02");
        entryDeux.setSlow_insu("0.03");
        entryDeux.setHba1c("2.00");
        entryDeux.setHour(heureMinute);
        entryDeux.setglycemy("32.4");
        entryDeux.setBreakfast(0);
        entryDeux.setLaunch(1);
        entryDeux.setDiner(2);
        entryDeux.setEncas(3);
        entryDeux.setSleep(4);
        entryDeux.setWakeup(5);
        entryDeux.setNight(6);
        entryDeux.setWorkout(7);
        entryDeux.setHypogly(8);
        entryDeux.setHypergly(9);
        entryDeux.setAtwork(10);
        entryDeux.setAthome(11);
        entryDeux.setAlcohol(12);
        entryDeux.setPeriod(13);
        listEntries.add(entryDeux);

        Gson gson = new Gson();
        JsonElement json = gson.toJsonTree(listEntries);
        System.out.println(json.toString());
        assertNotNull(json);
    }

}