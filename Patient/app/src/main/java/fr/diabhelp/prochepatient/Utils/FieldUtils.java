package fr.diabhelp.prochepatient.Utils;

import android.text.Editable;

/**
 * Created by Sumbers on 15/07/2016.
 */
public class FieldUtils {
    public static short MIN_REQUIRED = 0;
    public static short MATCH_REQUIRED = 1;

    public static Boolean isStringValid(String string, int required, short whichCheck)
    {
        Boolean is = false;

        if (string == null)
            return (false);
        if (whichCheck == MIN_REQUIRED) {
            if (string != null && !string.isEmpty() && string.length() >= required)
                is = true;
        }
        else if (whichCheck == MATCH_REQUIRED) {
            System.out.println("longueur de la chaine = " + string.length());
            if (string != null && !string.isEmpty() && string.length() == required){
                is = true;
            }
        }
        return (is);
    }

    public static Boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    public static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }
}
