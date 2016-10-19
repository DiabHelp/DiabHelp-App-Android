package fr.diabhelp.medecin_patient.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.medecin_patient.ChatMessage;
import fr.diabhelp.medecin_patient.Utils.JsonUtils;

/**
 * Created by Sundava on 19/10/2016.
 */
public class ResponseChatMessage {
    public String error;
    public ArrayList<ChatMessage> chatMessages;


    public ResponseChatMessage(String message) {
        this.error = message;
    }

    public ResponseChatMessage(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                Log.d("ChatMessageJSON","Success status OK");
                chatMessages = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "messages");
                for (int i = 0; i < arr.length(); i++)
                {
                    Log.d("ChatMessageJSON","Loop in msg, i = " + i);
                    //String sender, String message, long timestamp, boolean isMe
                    JSONObject jsonMessage = JsonUtils.getObjFromArray(arr, i);
                    String content =  JsonUtils.getStringFromKey(jsonMessage, "content");
                    long timestamp =  JsonUtils.getLongFromKey(jsonMessage, "timestamp");
                    String sender =  JsonUtils.getStringFromKey(jsonMessage, "sender");
                    Boolean isMe = sender.equals("Moi");
                    ChatMessage message = new ChatMessage(sender, content, timestamp, isMe);
                    Log.d("ChatMessageJSON","Loop in msg done" + i);
                    chatMessages.add(message);
                }
            }
            else
                this.error = ApiErrors.NO_USERS_FOUND.getServerMessage();
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            this.error = ApiErrors.SERVER_ERROR.getServerMessage();
        }
    }

    public String getError() {
        return this.error;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setchatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
