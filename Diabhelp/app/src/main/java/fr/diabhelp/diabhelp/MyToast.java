package fr.diabhelp.diabhelp;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sumbers on 09/10/2015.
 */
public class MyToast {
    private static MyToast instance = null;
    //private Activity ActInstance = null;

    private MyToast(){}

    public static MyToast getInstance()
    {
        if (instance == null)
        {
            instance = new MyToast();
        }
        return instance;
    }

    public void displayCustomWarningMessage(String message, int messageColor, int backgroundColor,  int xmlFile, int layoutToDisplay, int displayTime, Activity actInstance)
    {
        actInstance.findViewById(layoutToDisplay).setBackgroundColor(backgroundColor);

        LayoutInflater inflater = LayoutInflater.from(actInstance);
        View layout = inflater.inflate(xmlFile,
                (ViewGroup) actInstance.findViewById(layoutToDisplay));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        text.setTextColor(messageColor);

        Toast toast = new Toast(actInstance);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(displayTime);
        toast.setView(layout);
        toast.show();
    }

    public void displayWarningMessage(String message, int displayTime, Activity actInstance)
    {
        Toast toast = Toast.makeText(actInstance, message, displayTime);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();
    }
}
