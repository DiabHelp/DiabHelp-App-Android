package fr.diabhelp.medecin_patient;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.diabhelp.medecin_patient.API.ApiService;
import fr.diabhelp.medecin_patient.API.RetrofitHelper;
import fr.diabhelp.medecin_patient.Listeners.ChatRecyclerListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by sundava on 09/03/16.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatMessageHolder>{

    private ArrayList<ChatMessage> _messageList;
    ChatRecyclerListener listener;
    private String APIToken;

    public ChatRecyclerAdapter(String APIToken, ChatRecyclerListener listener, ArrayList<ChatMessage> chatMessages)
    {
        this.APIToken = APIToken;
        this.listener = listener;
        _messageList = chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this._messageList = chatMessages;
        this.notifyDataSetChanged();
    }

    public static class ChatMessageHolder extends RecyclerView.ViewHolder {
        private TextView sender;
        private TextView message;
        private TextView date;
        private LinearLayout content;
        private LinearLayout contentWithBG;

        public ChatMessageHolder (final View itemView)
        {
            super(itemView);
            this.sender = (TextView) itemView.findViewById(R.id.chat_sender);
            this.message = (TextView) itemView.findViewById(R.id.chat_msg);
            this.date = (TextView) itemView.findViewById(R.id.chat_date);
            this.content = (LinearLayout) itemView.findViewById(R.id.content);
            this.contentWithBG = (LinearLayout) itemView.findViewById(R.id.contentWithBackground);
        }
    }

    @Override
    public ChatMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_bubble, parent, false);
        ChatMessageHolder chatMessageHolder = new ChatMessageHolder(view);
        return chatMessageHolder;
    }

    @Override
    public void onBindViewHolder(ChatMessageHolder holder, int position) {
        holder.sender.setText(_messageList.get(position).getSender());
        setAlignement(holder, _messageList.get(position).isMe());
        holder.message.setText(_messageList.get(position).getMessage());
        holder.date.setText(_messageList.get(position).getDate());
    }

    private void setAlignement(ChatMessageHolder holder, boolean isMe) {

        if (isMe)
        {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.message.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.message.setLayoutParams(layoutParams);

            holder.sender.setVisibility(View.GONE);

            layoutParams = (LinearLayout.LayoutParams) holder.date.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.date.setLayoutParams(layoutParams);
        }
            else
        {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.message.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.message.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.sender.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.sender.setLayoutParams(layoutParams);
            holder.sender.setVisibility(View.VISIBLE);

            layoutParams = (LinearLayout.LayoutParams) holder.date.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.date.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return _messageList.size();
    }


    private ArrayList<ChatMessage> getMessageHistoryDebug(int min_messages) {
        //Utilise a l'epoque pour generer de la data sans l'API
        ArrayList<ChatMessage> messages = new ArrayList<>();
        /* DEBUG BLOCK*/
        int day_ms = 24 * 60 * 60 * 1000;
        int hour_ms = 60 * 60 * 1000;
        int minute_ms = 60 * 1000;
        messages.add(new ChatMessage("Moi", "Bonjour, j'aurais une question", new Date().getTime() - (hour_ms * 1), true));
        messages.add(new ChatMessage("Dr Delat", "Je vous écoute", new Date().getTime() - (48 * minute_ms + 22 * 1000), false));
        messages.add(new ChatMessage("Moi", "J'ai oublié mon injection d'insuline du repas de midi, que devrais-je faire ?", new Date().getTime() - (minute_ms * 44 + 10 * 1000), true));
        messages.add(new ChatMessage("Dr Delat", "Vous devez reprendre votre glycémie, puis ajuster la dose d'insuline en fonction", new Date().getTime() - (minute_ms* 42  + 35 * 1000), false));
        messages.add(new ChatMessage("Dr Delat", "Gardez en tête que l'insuline agira donc plus tard, puisque l'injection aura été retardée", new Date().getTime() - (minute_ms* 42  + 2 * 1000), false));
        messages.add(new ChatMessage("Moi", "Très bien, merci", new Date().getTime() - (minute_ms * 38 + 9 * 1000), true));
        messages.add(new ChatMessage("Dr Delat", "Pas de problème", new Date().getTime() - (minute_ms * 36 + 47 * 1000), false));
        messages.add(new ChatMessage("Dr Delat", "Surveillez votre glycémie durant les prochaines 24, et revenez vers moi si vous constatez des valeurs trop faibles ou trop élevées", new Date().getTime() - (minute_ms * 35 + 11 * 1000), false));

        /* END DEBUG */
        return messages;
    }

    public boolean addMessage(String msg)
    {
        return addMessage("Moi", msg, Calendar.getInstance().getTimeInMillis(), true);
    }

    public boolean addMessage(String msg, String sender, long timestamp, boolean isMe)
    {
        ChatMessage newmsg = new ChatMessage(msg, sender, timestamp, isMe);
        _messageList.add(newmsg);
        notifyDataSetChanged();
        return true;
    }
}
