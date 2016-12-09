package fr.diabhelp.patient.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sumbers on 25/07/2016.
 */
public class DateUtils {

    public static String CB_DATE_FORMAT = "MM/yy";
    public static String SERVER_DATE_FORMAT = "yyyy-MM-dd";

    public static Boolean isBefore(String d, String format, Date d1)
    {
        Boolean is = false;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            Date dConvert = sf.parse(d);
            if (dConvert.before(d1))
                is = true;
        } catch (ParseException e) {
            is = null;
            e.printStackTrace();
        }
        return (is);
    }
}
