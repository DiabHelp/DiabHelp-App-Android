package fr.diabhelp.medecin_patient;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sundava on 09/03/16.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatMessageHolder>{

    private ArrayList<ChatMessage> _messageList;
    private String APIToken;

    public ChatRecyclerAdapter(String APIToken)
    {
        this.APIToken = APIToken;
        _messageList = getMessageHistory(5);
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

    private ArrayList<ChatMessage> getMessageHistory(int min_messages) {
        //TODO : Connexion API pour des vraies data
        ArrayList<ChatMessage> messages = new ArrayList<>();
        /* DEBUG BLOCK*/
        for (int i = 0; i < min_messages; i++)
            messages.add(new ChatMessage("TestSender", "TestMsg" + i, new Date().getTime(), true));
        messages.add(new ChatMessage("TestSender", "TestMsg 2Days", new Date().getTime() - (24 * 60 * 60 * 1000 * 2), false));
        messages.add(new ChatMessage("TestSender", "TestMsg 3Days", new Date().getTime() - (24 * 60 * 60 * 1000 * 3), false));
        messages.add(new ChatMessage("TestSender", "TestMsg 10Days", new Date().getTime() - (24 * 60 * 60 * 1000 * 10), true));
        messages.add(new ChatMessage("TestSender", "TestMsg 2 Hours", new Date().getTime() - (60 * 60 * 1000 * 2), false));
        messages.add(new ChatMessage("TestSender", "TestMsg 2 Min", new Date().getTime() - (60 * 1000 * 2), true));
        messages.add(new ChatMessage("TestSender", "TestMsg 50 Sec", new Date().getTime() - (1000 * 50), false));

        /* END DEBUG */
        return messages;
    }

    public boolean addMessage(String msg)
    {
        double lol =  Math.random();
        return addMessage("Me", msg + " " + lol, Calendar.getInstance().getTimeInMillis(), true) && (lol % 2 == 0) && addMessage("Other", "Answer to " + msg, Calendar.getInstance().getTimeInMillis(), false);
    }

    public boolean addMessage(String msg, String sender, long timestamp, boolean isMe)
    {
        ChatMessage newmsg = new ChatMessage(msg, sender, timestamp, isMe);
        _messageList.add(newmsg);
        notifyDataSetChanged();
        return true;
    }
}
