package fr.diabhelp.carnetdesuivi.Carnet.Statistics;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfStatsDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfStats;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by vigour_a on 02/02/2016.
 */

public class StatisticsPersoFragment extends Fragment implements View.OnClickListener {

    View rootView;

    List<Integer> isActiveicon;

    private int ACTUAL_DATE_PICKER_ID = 0;
    private int BEGIN_DATE_PICKER_ID = 1;
    private int END_DATE_PICKER_ID = 2;

    private EditText _startDate, _endDate;

    private Calendar _startCalendar, _endCalendar;
    private DAO dao = null;
    private SQLiteDatabase db = null;

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
    private FloatingActionButton _showForm;

    private ScrollView mainLayout;
    private RelativeLayout graphLayout;

    private Button _send_data;

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    private ArrayList<EntryOfCDS> mall = null;
    private EntryOfStats statEntry = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dao = DAO.getInstance(getContext());
        db = dao.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.statistics_perso_fragment, container, false);

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

        _showForm = (FloatingActionButton) rootView.findViewById(R.id.show_form);
        _showForm.setOnClickListener(this);

        chart = (ColumnChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        mainLayout = (ScrollView) getActivity().findViewById(R.id.layout_statistics_perso);
        graphLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_graph_perso);

        getLastGraph();
        initForm();
        getAndSetData();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        onClickSwitch(v.getId());
        if (v.getId() != R.id.startDate && v.getId() != R.id.endDate && v.getId() != R.id.show_form)
            launchGraph();
    }

    public void onClickSwitch(int id) {
        switch (id) {
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
            case R.id.layout_breakfast:
                ImageView breakfastImg = (ImageView) rootView.findViewById(R.id.imgptidej);
                addStatus(breakfastImg, IconeType.BREAKFAST.getValue(), R.drawable.ptidej, R.drawable.ptidejgreen);
                break;
            case R.id.lunch:
                ImageView lunchImg = (ImageView) rootView.findViewById(R.id.imglunch);
                addStatus(lunchImg, IconeType.LUNCH.getValue(), R.drawable.lunch, R.drawable.lunchgreen);
                break;
            case R.id.diner:
                ImageView dinerImg = (ImageView) rootView.findViewById(R.id.imgdiner);
                addStatus(dinerImg, IconeType.DINER.getValue(), R.drawable.diner, R.drawable.dinergreen);
                break;
            case R.id.layout_encas:
                ImageView encasImg = (ImageView) rootView.findViewById(R.id.imgencas);
                addStatus(encasImg, IconeType.ENCAS.getValue(), R.drawable.cassecroute, R.drawable.cassecroutegreen);
                break;
            case R.id.layout_sleep:
                ImageView coucherImg = (ImageView) rootView.findViewById(R.id.imgcoucher);
                addStatus(coucherImg, IconeType.SLEEP.getValue(), R.drawable.coucher, R.drawable.couchergreen);
                break;
            case R.id.layout_wakeup:
                ImageView wakeupImg = (ImageView) rootView.findViewById(R.id.imglevee);
                addStatus(wakeupImg, IconeType.WAKEUP.getValue(), R.drawable.reveil, R.drawable.reveilgreen);
                break;
            case R.id.layout_night:
                ImageView nuitImg = (ImageView) rootView.findViewById(R.id.imgnuit);
                addStatus(nuitImg, IconeType.NIGHT.getValue(), R.drawable.night, R.drawable.nightgreen);
                break;
            case R.id.layout_sport:
                ImageView sportImg = (ImageView) rootView.findViewById(R.id.imgsport);
                addStatus(sportImg, IconeType.WORKOUT.getValue(), R.drawable.sport, R.drawable.sportgreen);
                break;
            case R.id.layout_hypo:
                ImageView hypoImg = (ImageView) rootView.findViewById(R.id.imghypo);
                addStatus(hypoImg, IconeType.HYPO.getValue(), R.drawable.hypo, R.drawable.hypogreen);
                break;
            case R.id.layout_hyper:
                ImageView hyperImg = (ImageView) rootView.findViewById(R.id.imghyper);
                addStatus(hyperImg, IconeType.HYPER.getValue(), R.drawable.hyper, R.drawable.hypergreen);
                break;
            case R.id.layout_work:
                ImageView workImg = (ImageView) rootView.findViewById(R.id.imgwork);
                addStatus(workImg, IconeType.WORK.getValue(), R.drawable.work, R.drawable.workgreen);
                break;
            case R.id.layout_home:
                ImageView homeImg = (ImageView) rootView.findViewById(R.id.imghome);
                addStatus(homeImg, IconeType.HOME.getValue(), R.drawable.home, R.drawable.homegreen);
                break;
            case R.id.layout_alcohol:
                ImageView alcoholImg = (ImageView) rootView.findViewById(R.id.imgalcool);
                addStatus(alcoholImg, IconeType.ALCOHOL.getValue(), R.drawable.alcool, R.drawable.alcoolgreen);
                break;
            case R.id.layout_period:
                ImageView periodImg = (ImageView) rootView.findViewById(R.id.imgperiod);
                addStatus(periodImg, IconeType.PERIOD.getValue(), R.drawable.period, R.drawable.periodgreen);
                break;
            case R.id.show_form:
                showForm();
                break;
        }
    }

    private void addStatus(ImageView img, int value, int Img, int ImgGreen) {
        if (isActiveicon.get(value) == 0) {
            isActiveicon.set(value, 1);
            img.setImageResource(ImgGreen);
        } else {
            isActiveicon.set(value, 0);
            img.setImageResource(Img);
        }
    }

    public void initForm() {
        isActiveicon = new ArrayList<Integer>();

        isActiveicon.add(IconeType.BREAKFAST.getValue(), statEntry.getBreakfast());
        isActiveicon.add(IconeType.LUNCH.getValue(), statEntry.getLaunch());
        isActiveicon.add(IconeType.DINER.getValue(), statEntry.getDiner());
        isActiveicon.add(IconeType.ENCAS.getValue(), statEntry.getEncas());
        isActiveicon.add(IconeType.SLEEP.getValue(), statEntry.getSleep());
        isActiveicon.add(IconeType.WAKEUP.getValue(), statEntry.getWakeup());
        isActiveicon.add(IconeType.NIGHT.getValue(), statEntry.getNight());
        isActiveicon.add(IconeType.WORKOUT.getValue(), statEntry.getWorkout());
        isActiveicon.add(IconeType.HYPO.getValue(), statEntry.getHypogly());
        isActiveicon.add(IconeType.HYPER.getValue(), statEntry.getHypergly());
        isActiveicon.add(IconeType.WORK.getValue(), statEntry.getAtwork());
        isActiveicon.add(IconeType.HOME.getValue(), statEntry.getAthome());
        isActiveicon.add(IconeType.ALCOHOL.getValue(), statEntry.getAlcohol());
        isActiveicon.add(IconeType.PERIOD.getValue(), statEntry.getPeriod());

        if (statEntry.getBreakfast() == 1) {
            isActiveicon.set(IconeType.BREAKFAST.getValue(), 0);
            onClickSwitch(R.id.layout_breakfast);
        }
        if (statEntry.getLaunch() == 1) {
            isActiveicon.set(IconeType.LUNCH.getValue(), 0);
            onClickSwitch(R.id.launch);
        }
        if (statEntry.getDiner() == 1) {
            isActiveicon.set(IconeType.DINER.getValue(), 0);
            onClickSwitch(R.id.diner);
        }
        if (statEntry.getEncas() == 1) {
            isActiveicon.set(IconeType.ENCAS.getValue(), 0);
            onClickSwitch(R.id.layout_encas);
        }
        if (statEntry.getSleep() == 1) {
            isActiveicon.set(IconeType.SLEEP.getValue(), 0);
            onClickSwitch(R.id.layout_sleep);
        }
        if (statEntry.getWakeup() == 1) {
            isActiveicon.set(IconeType.WAKEUP.getValue(), 0);
            onClickSwitch(R.id.layout_wakeup);
        }
        if (statEntry.getNight() == 1) {
            isActiveicon.set(IconeType.NIGHT.getValue(), 0);
            onClickSwitch(R.id.layout_night);
        }
        if (statEntry.getWorkout() == 1) {
            isActiveicon.set(IconeType.WORKOUT.getValue(), 0);
            onClickSwitch(R.id.layout_sport);
        }
        if (statEntry.getHypogly() == 1) {
            isActiveicon.set(IconeType.HYPO.getValue(), 0);
            onClickSwitch(R.id.layout_hypo);
        }
        if (statEntry.getHypergly() == 1) {
            isActiveicon.set(IconeType.HYPER.getValue(), 0);
            onClickSwitch(R.id.layout_hyper);
        }
        if (statEntry.getAtwork() == 1) {
            isActiveicon.set(IconeType.WORK.getValue(), 0);
            onClickSwitch(R.id.layout_work);
        }
        if (statEntry.getAthome() == 1) {
            isActiveicon.set(IconeType.HOME.getValue(), 0);
            onClickSwitch(R.id.layout_home);
        }
        if (statEntry.getAlcohol() == 1) {
            isActiveicon.set(IconeType.ALCOHOL.getValue(), 0);
            onClickSwitch(R.id.layout_alcohol);
        }
        if (statEntry.getPeriod() == 1) {
            isActiveicon.set(IconeType.PERIOD.getValue(), 0);
            onClickSwitch(R.id.layout_period);
        }

        _startDate.setText(statEntry.getBeg_date());
        _endDate.setText(statEntry.getEnd_date());
    }

    private void getLastGraph() {
        statEntry = EntryOfStatsDAO.selectStat(db);
        if (statEntry == null) {
            getAllData();
            statEntry = new EntryOfStats();
            statEntry.setAlcohol(0);
            statEntry.setAthome(0);
            statEntry.setAtwork(0);
            statEntry.setBreakfast(0);
            statEntry.setDiner(0);
            statEntry.setEncas(0);
            statEntry.setHypergly(0);
            statEntry.setHypogly(0);
            statEntry.setLaunch(0);
            statEntry.setNight(0);
            statEntry.setPeriod(0);
            statEntry.setSleep(0);
            statEntry.setWakeup(0);
            statEntry.setWorkout(0);
            statEntry.setBeg_date("Date de début"); // 00-00-00
            statEntry.setEnd_date("Date de fin");
        }
    }

    private void setStatEntry() {
        if (isActiveicon.get(IconeType.BREAKFAST.getValue()) == 1)
            statEntry.setBreakfast(1);
        else
            statEntry.setBreakfast(0);
        if (isActiveicon.get(IconeType.LUNCH.getValue()) == 1)
            statEntry.setLaunch(1);
        else
            statEntry.setLaunch(0);
        if (isActiveicon.get(IconeType.DINER.getValue()) == 1)
            statEntry.setDiner(1);
        else
            statEntry.setDiner(0);
        if (isActiveicon.get(IconeType.ENCAS.getValue()) == 1)
            statEntry.setEncas(1);
        else
            statEntry.setEncas(0);
        if (isActiveicon.get(IconeType.SLEEP.getValue()) == 1)
            statEntry.setSleep(1);
        else
            statEntry.setSleep(0);
        if (isActiveicon.get(IconeType.WAKEUP.getValue()) == 1)
            statEntry.setWakeup(1);
        else
            statEntry.setWakeup(0);
        if (isActiveicon.get(IconeType.NIGHT.getValue()) == 1)
            statEntry.setNight(1);
        else
            statEntry.setNight(0);
        if (isActiveicon.get(IconeType.WORKOUT.getValue()) == 1)
            statEntry.setWorkout(1);
        else
            statEntry.setWorkout(0);
        if (isActiveicon.get(IconeType.HYPO.getValue()) == 1)
            statEntry.setHypogly(1);
        else
            statEntry.setHypogly(0);
        if (isActiveicon.get(IconeType.HYPER.getValue()) == 1)
            statEntry.setHypergly(1);
        else
            statEntry.setHypergly(0);
        if (isActiveicon.get(IconeType.WORK.getValue()) == 1)
            statEntry.setAtwork(1);
        else
            statEntry.setAtwork(0);
        if (isActiveicon.get(IconeType.HOME.getValue()) == 1)
            statEntry.setAthome(1);
        else
            statEntry.setAthome(0);
        if (isActiveicon.get(IconeType.ALCOHOL.getValue()) == 1)
            statEntry.setAlcohol(1);
        else
            statEntry.setAlcohol(0);
        if (isActiveicon.get(IconeType.PERIOD.getValue()) == 1)
            statEntry.setPeriod(1);
        else
            statEntry.setPeriod(0);
    }

    private void getAllData() {
        mall = EntryOfCDSDAO.selectAll(Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""), db);

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<>();

        values = new ArrayList<SubcolumnValue>();

        for (int j = 0; j < mall.size(); ++j) {
            double val = mall.get(j).getglycemy();
            float f = (float) val;
            if (val > 125 || val < 75)
                values.add(new SubcolumnValue(f, Color.parseColor("#FF4444")));
            else
                values.add(new SubcolumnValue(f, Color.parseColor("#99CC00")));
        }

        Column column = new Column(values);
        column.setHasLabels(hasLabels);
        column.setHasLabelsOnlyForSelected(hasLabelForSelected);
        columns.add(column);

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Jours");
                axisY.setName("Glycémie");
            }
            data.setAxisXBottom(null);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setColumnChartData(data);
    }

    private boolean getAndSetData() {

        try {
            _startCalendar = stringToCalendar(statEntry.getBeg_date());
            _endCalendar = stringToCalendar(statEntry.getEnd_date());
        } catch (ParseException e) {
            Log.e("Date Parsing Exception", e.getMessage());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String startDate = sdf.format(_startCalendar.getTime());
        String endDate = sdf.format(_endCalendar.getTime());

        setStatEntry();
        EntryOfStatsDAO.addStat(statEntry, db);
        mall = EntryOfCDSDAO.selectDayDateAndIcone(startDate, endDate, String.valueOf(isActiveicon.get(IconeType.BREAKFAST.getValue())), String.valueOf(isActiveicon.get(IconeType.LUNCH.getValue())), String.valueOf(isActiveicon.get(IconeType.DINER.getValue())),
                String.valueOf(isActiveicon.get(IconeType.ENCAS.getValue())), String.valueOf(isActiveicon.get(IconeType.SLEEP.getValue())), String.valueOf(isActiveicon.get(IconeType.WAKEUP.getValue())), String.valueOf(isActiveicon.get(IconeType.NIGHT.getValue())),
                String.valueOf(isActiveicon.get(IconeType.WORKOUT.getValue())), String.valueOf(isActiveicon.get(IconeType.HYPO.getValue())), String.valueOf(isActiveicon.get(IconeType.HYPER.getValue())), String.valueOf(isActiveicon.get(IconeType.WORK.getValue())),
                String.valueOf(isActiveicon.get(IconeType.HOME.getValue())), String.valueOf(isActiveicon.get(IconeType.ALCOHOL.getValue())), String.valueOf(isActiveicon.get(IconeType.PERIOD.getValue())), db);

        if (mall == null)
            return false;

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;

        values = new ArrayList<SubcolumnValue>();

        for (int j = 0; j < mall.size(); ++j) {
            double val = mall.get(j).getglycemy();
            float f = (float) val;
            if (val > 125 || val < 75)
                values.add(new SubcolumnValue(f, Color.parseColor("#FF4444")));
            else
                values.add(new SubcolumnValue(f, Color.parseColor("#99CC00")));
        }

        Column column = new Column(values);
        column.setHasLabels(hasLabels);
        column.setHasLabelsOnlyForSelected(hasLabelForSelected);
        columns.add(column);

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Jours");
                axisY.setName("Glycémie");
            }
            data.setAxisXBottom(null);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);
        return true;
    }

    private void launchGraph() {
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

            try {
                _startCalendar = stringToCalendar(_startDate.getText().toString());
                _endCalendar = stringToCalendar(_endDate.getText().toString());
            } catch (ParseException e) {
                Log.e("Date Parsing Exception", e.getMessage());
            }

            long start = _startCalendar.getTimeInMillis();
            long end = _endCalendar.getTimeInMillis();
            if (start > end) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Dates invalides");
                alertDialog.setMessage("Veuillez renseigner une date de début inférieure à celle de fin.");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            } else {
                if (getAndSetData())
                    chart.startDataAnimation();
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Aucun résultat");
                    alertDialog.setMessage("Auncun résultat n'a été trouvé.");
                    alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
            }
        }
    }

    public void showForm() {
        mainLayout = (ScrollView) getActivity().findViewById(R.id.layout_statistics_perso);

        if (mainLayout.getVisibility() == LinearLayout.INVISIBLE) {
            Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_up);

            mainLayout.bringToFront();
            mainLayout.setVisibility(LinearLayout.VISIBLE);
            mainLayout.startAnimation(slide_up);
        } else {
            Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                    R.anim.slide_down);

            mainLayout.startAnimation(slide_down);
            mainLayout.setVisibility(LinearLayout.INVISIBLE);
        }
    }

    public static Calendar stringToCalendar(String strDate) throws ParseException {
        String current_date = strDate.substring(6) + "-" + strDate.substring(3, 5) + "-" + strDate.substring(0, 2) + " 00:00:00";
        String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        Date date = sdf.parse(current_date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
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

            if (ACTUAL_DATE_PICKER_ID == BEGIN_DATE_PICKER_ID) {
                _startDate.setText(formattedDate);
                statEntry.setBeg_date(formattedDate);
                _startCalendar = c;
            }
            else if (ACTUAL_DATE_PICKER_ID == END_DATE_PICKER_ID) {
                _endDate.setText(formattedDate);
                statEntry.setEnd_date(formattedDate);
                _endCalendar = c;
            }
            launchGraph();
        }
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
//            Toast.makeText(getActivity(), "Glycémie: " + value.getValue(), Toast.LENGTH_SHORT).show();
            final EntryOfCDS entry = mall.get(subcolumnIndex);
            GoToEntry go = new GoToEntry(entry, getContext(), getActivity());
        }

        @Override
        public void onValueDeselected() {

        }

    }

}

