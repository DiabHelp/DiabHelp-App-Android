package fr.diabhelp.carnetdesuivi.Carnet.Statistics;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import fr.diabhelp.carnetdesuivi.Carnet.EntryActivity;
import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.R;

/**
 * Created by vigour_a on 16/03/2016.
 */
public class GoToEntry {

    public GoToEntry(EntryOfCDS entry, Context context, Activity activity) {
        final EntryOfCDS ent = entry;
        final Activity act = activity;

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.graph_entry_view);

        TextView txt = (TextView)dialog.findViewById(R.id.textbox);
        Button button = (Button)dialog.findViewById(R.id.goToEntry);

        String day = entry.getDate().toString().substring(3, 5);
        String month = entry.getDate().toString().substring(0, 2);
        String year = entry.getDate().toString().substring(6, 10);
        String formated_date = day + "-" + month + "-" + year;

        txt.setText("Titre : " + entry.getTitle() +
                    "\nDate : " + formated_date +
                    "\nGlycémie : " + entry.getglycemy());

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToEntry(ent, act);
            }
        });

        dialog.show();
    }

    public void goToEntry(EntryOfCDS entry, Activity activity) {
        Intent intent = new Intent(activity, EntryActivity.class);

        intent.putExtra("title", entry.getTitle());
        intent.putExtra("place", entry.getPlace());
        intent.putExtra("glucide", entry.getGlucide());
        intent.putExtra("activity", entry.getActivity());
        intent.putExtra("activityType", entry.getActivityType());
        intent.putExtra("notes", entry.getNotes());
        intent.putExtra("date", entry.getDate());
        intent.putExtra("fast_insu", entry.getFast_insu());
        intent.putExtra("slow_insu", entry.getSlow_insu());
        intent.putExtra("hba1c", entry.getHba1c());
        intent.putExtra("hour", entry.getHour());
        intent.putExtra("date", entry.getDate());
        intent.putExtra("glycemy", entry.getglycemy());
        intent.putExtra("launch", entry.getLaunch());
        intent.putExtra("diner", entry.getDiner());
        intent.putExtra("encas", entry.getEncas());
        intent.putExtra("sleep", entry.getSleep());
        intent.putExtra("wakeup", entry.getWakeup());
        intent.putExtra("night", entry.getNight());
        intent.putExtra("workout", entry.getWorkout());
        intent.putExtra("hypogly", entry.getHypogly());
        intent.putExtra("hypergly", entry.getHypergly());
        intent.putExtra("atwork", entry.getAtwork());
        intent.putExtra("athome", entry.getAthome());
        intent.putExtra("alcohol", entry.getAlcohol());
        intent.putExtra("period", entry.getPeriod());
        intent.putExtra("breakfast", entry.getBreakfast());

        activity.startActivity(intent);
//        activity.finish();
    }

}
