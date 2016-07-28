package fr.diabhelp.diabhelp.Utils;

import android.util.Patterns;

/**
 * Created by Sumbers on 28/07/2016.
 */
public class StringUtils {

    public static final int MIN_LENGTH = 0;
    public static final int MATCH_LENGTH = 1;


    public static boolean hasFieldGoodFormat(String str, int typeComparaison, int length)
    {
        boolean has = false;

        switch (typeComparaison)
        {
            case StringUtils.MIN_LENGTH:
                if (str != null && str.length() >= length)
                    has = true;
                break;
            case StringUtils.MATCH_LENGTH:
                if (str != null && str.length() == length)
                    has = true;
                break;
        }
        return has;
    }

    public static Boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
