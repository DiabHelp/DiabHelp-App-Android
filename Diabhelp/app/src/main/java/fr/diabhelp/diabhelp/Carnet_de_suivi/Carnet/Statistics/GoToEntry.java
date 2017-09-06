package fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.Statistics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnet.EntryActivity;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Utils.DateMagnifier;
import fr.diabhelp.diabhelp.R;

/**
 * Created by vigour_a on 16/03/2016.
 */
public class GoToEntry {

    public GoToEntry(final EntryOfCDS entry, Context context, final Activity activity) {
        final EntryOfCDS ent = entry;
        final Activity act = activity;
        LayoutInflater factory = LayoutInflater.from(context);

//        Dialog dialog = new Dialog(context);
        final View alertDialogView = factory.inflate(R.layout.graph_entry_view, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(alertDialogView);

/*        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.graph_entry_view);*/

        TextView txtdate = (TextView) alertDialogView.findViewById(R.id.textboxdate);
        TextView txtgly = (TextView) alertDialogView.findViewById(R.id.textboxgly);
        TextView txtcalo = (TextView) alertDialogView.findViewById(R.id.textboxcalo);

        String day = entry.getDateCreation().toString().substring(3, 5);
        String month = entry.getDateCreation().toString().substring(0, 2);
        String year = entry.getDateCreation().toString().substring(6, 10);
        String formated_date = month + "-" + day + "-" + year;
        DateMagnifier dt = new DateMagnifier();
        dialog.setTitle(entry.getTitle());
        dialog.setIcon(R.drawable.diab_logo);

        txtdate.setText("Date : " + dt.getCleanDate(formated_date));
        txtgly.setText("Glycémie : " + entry.getglycemy());


        dialog.setPositiveButton("Modifier",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goToEntry(entry, activity);

                    }
                });

        dialog.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    }
                });

        dialog.show();
    }

    public void goToEntry(EntryOfCDS entry, Activity activity) {
        Intent intent = new Intent(activity, EntryActivity.class);

        intent.putExtra("title", entry.getTitle());
        intent.putExtra("place", entry.getPlace());
        intent.putExtra("glucide", entry.getGlucide());
        intent.putExtra("activitytime", entry.getActivity());
        intent.putExtra("activityType", entry.getActivityType());
        intent.putExtra("notes", entry.getNotes());
        intent.putExtra("date", entry.getDateCreation());
        intent.putExtra("fastInsu", entry.getFast_insu());
        intent.putExtra("slowInsu", entry.getSlow_insu());
        intent.putExtra("hba1c", entry.getHba1c());
        intent.putExtra("hour", entry.getHour());
        intent.putExtra("date", entry.getDateCreation());
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
        intent.putExtra("work", entry.getAtwork());
        intent.putExtra("athome", entry.getAthome());
        intent.putExtra("alcohol", entry.getAlcohol());
        intent.putExtra("period", entry.getPeriod());
        intent.putExtra("breakfast", entry.getBreakfast());
        intent.putExtra("activity", "stat");

        activity.startActivity(intent);
        activity.finish();
    }

}
