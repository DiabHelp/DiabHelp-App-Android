package fr.diabhelp.carnetdesuivi.Carnet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;

/**
 * Created by vigour_a on 02/02/2016.
 */

public class StatisticsPersoFragment extends Fragment implements View.OnClickListener {

    View rootView;

    List<Integer> isActiveicon;

    private int ACTUAL_DATE_PICKER_ID = 0;
    private int BEGIN_DATE_PICKER_ID = 1;
    private int END_DATE_PICKER_ID = 2;

    private EditText _startDate;
    private EditText _endDate;

    public enum IconeType {
        BREAKFAST(0),
        LUNCH(1),
        DINER(2),
        ENCAS(3),
        SLEEP(4),
        WAKEUP(5),
        NIGHT(6),
        WORKOUT(7),
        HYPO(8),
        HYPER(9),
        WORK(10),
        HOME(11),
        ALCOHOL(12),
        PERIOD(13);

        private final int value;

        private IconeType(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    private LinearLayout _alcohol, _lunch, _diner, _encas, _sleep, _wakeup, _night, _workout, _hypogly, _hypergly, _atwork, _athome, _period, _breakfast;

    private Button _send_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.statistics_perso_fragment, container, false);

        init_icon();

        _startDate = (EditText) rootView.findViewById(R.id.startDate);
        _startDate.setOnClickListener(this);

        _endDate = (EditText) rootView.findViewById(R.id.endDate);
        _endDate.setOnClickListener(this);

        _alcohol = (LinearLayout) rootView.findViewById(R.id.layout_alcohol);
        _alcohol.setOnClickListener(this);

        _athome = (LinearLayout) rootView.findViewById(R.id.layout_home);
        _athome.setOnClickListener(this);

        _atwork = (LinearLayout) rootView.findViewById(R.id.layout_work);
        _atwork.setOnClickListener(this);

        _breakfast = (LinearLayout) rootView.findViewById(R.id.layout_breakfast);
        _breakfast.setOnClickListener(this);

        _diner = (LinearLayout) rootView.findViewById(R.id.diner);
        _diner.setOnClickListener(this);

        _encas = (LinearLayout) rootView.findViewById(R.id.layout_encas);
        _encas.setOnClickListener(this);

        _hypergly = (LinearLayout) rootView.findViewById(R.id.layout_hyper);
        _hypergly.setOnClickListener(this);

        _hypogly = (LinearLayout) rootView.findViewById(R.id.layout_hypo);
        _hypogly.setOnClickListener(this);

        _lunch = (LinearLayout) rootView.findViewById(R.id.lunch);
        _lunch.setOnClickListener(this);

        _night = (LinearLayout) rootView.findViewById(R.id.layout_night);
        _night.setOnClickListener(this);

        _period = (LinearLayout) rootView.findViewById(R.id.layout_period);
        _period.setOnClickListener(this);

        _sleep = (LinearLayout) rootView.findViewById(R.id.layout_sleep);
        _sleep.setOnClickListener(this);

        _wakeup = (LinearLayout) rootView.findViewById(R.id.layout_wakeup);
        _wakeup.setOnClickListener(this);

        _workout = (LinearLayout) rootView.findViewById(R.id.layout_sport);
        _workout.setOnClickListener(this);

        _send_data = (Button) rootView.findViewById(R.id.layout_send_data);
        _send_data.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                ACTUAL_DATE_PICKER_ID = 1;
                DialogFragment startPicker = new DatePickerFragment();
                startPicker.show(getActivity().getFragmentManager(), "datePicker");
                break;
            case R.id.endDate:
                ACTUAL_DATE_PICKER_ID = 2;
                DialogFragment endPicker = new DatePickerFragment();
                endPicker.show(getActivity().getFragmentManager(), "datePicker");
                break;
            case R.id.layout_send_data:
                launch_graph();
                break;
            case R.id.layout_breakfast:
                ImageView breakfastImg = (ImageView) rootView.findViewById(R.id.imgptidej);
                add_status(breakfastImg, IconeType.BREAKFAST.getValue(), R.drawable.ptidej, R.drawable.ptidejgreen);
                break;
            case R.id.lunch:
                ImageView lunchImg = (ImageView) rootView.findViewById(R.id.imglunch);
                add_status(lunchImg, IconeType.LUNCH.getValue(), R.drawable.lunch, R.drawable.lunchgreen);
                break;
            case R.id.diner:
                ImageView dinerImg = (ImageView) rootView.findViewById(R.id.imgdiner);
                add_status(dinerImg, IconeType.DINER.getValue(), R.drawable.diner, R.drawable.dinergreen);
                break;
            case R.id.layout_encas:
                ImageView encasImg = (ImageView) rootView.findViewById(R.id.imgencas);
                add_status(encasImg, IconeType.ENCAS.getValue(), R.drawable.cassecroute, R.drawable.cassecroutegreen);
                break;
            case R.id.layout_sleep:
                ImageView coucherImg = (ImageView) rootView.findViewById(R.id.imgcoucher);
                add_status(coucherImg, IconeType.SLEEP.getValue(), R.drawable.coucher, R.drawable.couchergreen);
                break;
            case R.id.layout_wakeup:
                ImageView wakeupImg = (ImageView) rootView.findViewById(R.id.imglevee);
                add_status(wakeupImg, IconeType.WAKEUP.getValue(), R.drawable.reveil, R.drawable.reveilgreen);
                break;
            case R.id.layout_night:
                ImageView nuitImg = (ImageView) rootView.findViewById(R.id.imgnuit);
                add_status(nuitImg, IconeType.NIGHT.getValue(), R.drawable.night, R.drawable.nightgreen);
                break;
            case R.id.layout_sport:
                ImageView sportImg = (ImageView) rootView.findViewById(R.id.imgsport);
                add_status(sportImg, IconeType.WORKOUT.getValue(), R.drawable.sport, R.drawable.sportgreen);
                break;
            case R.id.layout_hypo:
                ImageView hypoImg = (ImageView) rootView.findViewById(R.id.imghypo);
                add_status(hypoImg, IconeType.HYPO.getValue(), R.drawable.hypo, R.drawable.hypogreen);
                break;
            case R.id.layout_hyper:
                ImageView hyperImg = (ImageView) rootView.findViewById(R.id.imghyper);
                add_status(hyperImg, IconeType.HYPER.getValue(), R.drawable.hyper, R.drawable.hypergreen);
                break;
            case R.id.layout_work:
                ImageView workImg = (ImageView) rootView.findViewById(R.id.imgwork);
                add_status(workImg, IconeType.WORK.getValue(), R.drawable.work, R.drawable.workgreen);
                break;
            case R.id.layout_home:
                ImageView homeImg = (ImageView) rootView.findViewById(R.id.imghome);
                add_status(homeImg, IconeType.HOME.getValue(), R.drawable.home, R.drawable.homegreen);
                break;
            case R.id.layout_alcohol:
                ImageView alcoholImg = (ImageView) rootView.findViewById(R.id.imgalcool);
                add_status(alcoholImg, IconeType.ALCOHOL.getValue(), R.drawable.alcool, R.drawable.alcoolgreen);
                break;
            case R.id.layout_period:
                ImageView periodImg = (ImageView) rootView.findViewById(R.id.imgperiod);
                add_status(periodImg, IconeType.PERIOD.getValue(), R.drawable.period, R.drawable.periodgreen);
                break;
        }
    }

    private void add_status(ImageView img, int value, int Img, int ImgGreen) {
        if (isActiveicon.get(value) == 0) {
            isActiveicon.set(value, 1);
            img.setImageResource(ImgGreen);
        } else {
            isActiveicon.set(value, 0);
            img.setImageResource(Img);
        }
    }

    public void init_icon() {
        isActiveicon = new ArrayList<Integer>();

        isActiveicon.add(IconeType.BREAKFAST.getValue(), 0);
        isActiveicon.add(IconeType.LUNCH.getValue(), 0);
        isActiveicon.add(IconeType.DINER.getValue(), 0);
        isActiveicon.add(IconeType.ENCAS.getValue(), 0);
        isActiveicon.add(IconeType.SLEEP.getValue(), 0);
        isActiveicon.add(IconeType.WAKEUP.getValue(), 0);
        isActiveicon.add(IconeType.NIGHT.getValue(), 0);
        isActiveicon.add(IconeType.WORKOUT.getValue(), 0);
        isActiveicon.add(IconeType.HYPO.getValue(), 0);
        isActiveicon.add(IconeType.HYPER.getValue(), 0);
        isActiveicon.add(IconeType.WORK.getValue(), 0);
        isActiveicon.add(IconeType.HOME.getValue(), 0);
        isActiveicon.add(IconeType.ALCOHOL.getValue(), 0);
        isActiveicon.add(IconeType.PERIOD.getValue(), 0);
    }

    private void launch_graph() {
        if (_startDate.getText().length() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Date de début manquante");
            alertDialog.setMessage("Veuillez renseigner une date de début.");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        } else if (_endDate.getText().length() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Date de fin manquante");
            alertDialog.setMessage("Veuillez renseigner une date de fin.");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        } else {
            Intent GraphPersointent = new Intent(getActivity(), GraphPersoActivity.class);
            startActivity(GraphPersointent);
        }
    }

    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sdf.format(c.getTime());
            if (ACTUAL_DATE_PICKER_ID == BEGIN_DATE_PICKER_ID)
                _startDate.setText(formattedDate);
            else if (ACTUAL_DATE_PICKER_ID == END_DATE_PICKER_ID)
                _endDate.setText(formattedDate);
        }
    }

}