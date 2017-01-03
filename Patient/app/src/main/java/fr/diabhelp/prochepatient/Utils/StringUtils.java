package fr.diabhelp.proche.Utils;

/**
 * Created by Sumbers on 25/07/2016.
 */
public class StringUtils {

    public static String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    public static String capitalize(String str)
    {
        return (str.substring(0,1).toUpperCase() + str.substring(1));
    }
}
