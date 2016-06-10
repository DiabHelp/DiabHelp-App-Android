package fr.diabhelp.carnetdesuivi.Utils;

import android.util.Log;

/**
 * Created by naqued on 06/06/16.
 */
public class DateMagnifier {

    protected String getMonthstr(String month)
    {
        switch (month)
        {
            case "01" :
                return ("Janvier");
            case "02" :
                return ("Fevrier");
            case "03" :
                return ("Mars");
            case "04" :
                return ("Avril");
            case "05" :
                return ("Mai");
            case "06" :
                return ("Juin");
            case "07" :
                return ("Juillet");
            case "08" :
                return ("Aout");
            case "09" :
                return ("Septembre");
            case "10" :
                return ("Octobre");
            case "11" :
                return ("Novembre");
            case "12" :
                return ("Decembre");
        }
        return null;
    }

    public String getCleanDate(String date)
    {
        String finaldate;
        finaldate = date.split("-")[1] + " ";
        finaldate += getMonthstr(date.split("-")[0]) + " ";
        finaldate += date.split("-")[2];
        return finaldate;
    }

}
