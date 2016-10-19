package fr.diabhelp.proche.Widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import fr.diabhelp.proche.R;


public class DialogActivity extends Activity {
    public static final String ARG_DIALOG_NUMBER = "arg_dialog_number";
    public Handler_gps _gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Should do a proper argument verification here
        boolean validArg = false;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(ARG_DIALOG_NUMBER)) {
                displayDialog(extras.getInt(ARG_DIALOG_NUMBER));
                validArg = true;
            }
        }

        if (!validArg) {
            displayDialog(1);
        }
    }

    private void displayDialog(int number) {
        LayoutInflater factory = LayoutInflater.from(this);

        final View alertDialogView = factory.inflate(R.layout.dialog, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Envoyer une alerte");
//        adb.setIcon(R.drawable.diab_logo);
        adb.setPositiveButton("Envoyer une alerte", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText pos = (EditText) alertDialogView.findViewById(R.id.position);
                EditText message = (EditText) alertDialogView.findViewById(R.id.message);

                String location = pos.getText().toString();
                Log.e("location", "location : " + location);


                DialogActivity.this.finish();
            }


        });
        adb.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DialogActivity.this.finish();
                    }
                });
        adb.show();
        _gps = new Handler_gps(this, (EditText) alertDialogView.findViewById(R.id.position));
        _gps.run();
    }
    private Context getDialogContext() {
                        final Context context;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            context = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light);
                        } else {
                            context = new ContextThemeWrapper(this, android.R.style.Theme_Dialog);
                        }

                        return context;
                    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
