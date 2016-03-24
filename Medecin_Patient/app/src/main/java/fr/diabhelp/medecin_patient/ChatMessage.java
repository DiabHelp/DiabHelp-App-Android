package fr.diabhelp.medecin_patient;

import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * Created by sundava on 09/03/16.
 */
public class ChatMessage {

    public ChatMessage(String sender,String message,long timestamp, boolean isMe)
    {
        this.sender = sender;
        this.message = message;
        this.isMe = isMe;
        long timediff = new Date().getTime() - timestamp;
        Date msgDate = new Date(timestamp);
        Log.d("Chat", "Timediff = " + timediff + " Date =  " + msgDate);
        if (timediff > 24 * 60 * 60 * 1000) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM HH:mm:ss ", Locale.FRANCE);
            this.date = simpleDateFormat.format(msgDate);
        }else {
            PrettyTime p = new PrettyTime();
            this.date = p.format(msgDate);
        }
    }


    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public boolean isMe() {return isMe;}

    private String sender;
    private String message;
    private String date;
    private boolean isMe;
}
