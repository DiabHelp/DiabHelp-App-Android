package fr.diabhelp.medecin_patient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }
    private RecyclerView recyclerView;
    private ChatRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager  recLayoutManager;
    String APIToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Medecin Patient", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        recyclerAdapter = new ChatRecyclerAdapter(APIToken);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1); // Scroll to bottom of messages
        final TextView textInput = (TextView)v.findViewById(R.id.msg_input);

        v.findViewById(R.id.button_send_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textInput.getText().toString();
                textInput.setText("");
                sendChatMessage(msg);
                recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
            }
        });
        return v;
    }

    private void sendChatMessage(String msg) {
        Log.d("Chat", "Sending message : " + msg);
        recyclerAdapter.addMessage(msg);

    }
}
