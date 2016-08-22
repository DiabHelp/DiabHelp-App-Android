package fr.diabhelp.carnetdesuivi.Carnet.Statistics;

/**
 * Created by vigour_a on 02/02/2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticsWeekFragment extends Fragment {

    private LineChartView chart;
    private LineChartData data;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private DAO dao = null;
    private SQLiteDatabase db = null;

    private ArrayList<EntryOfCDS> mall = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dao = DAO.getInstance(getContext());
        db = dao.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistics_week_fragment, container, false);

        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        getData();

        chart.setViewportCalculationEnabled(false);

        return rootView;
    }


    private void getData() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        String endDate = sdf.format(c.getTime());

        c.add(Calendar.DATE, -7);
        String startDate = sdf.format(c.getTime());

        mall = EntryOfCDSDAO.selectBetweenDays(startDate, endDate, Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""), db);
        Log.e("size mall", "t:" + String.valueOf(mall.size()));

        List<Line> lines = new ArrayList<Line>();
        List<AxisValue> axisValues = new ArrayList<>();

        List<PointValue> values = new ArrayList<PointValue>();

        if (mall.size() == 1)
            values.add(new PointValue(-1, 0));

        for (int j = 0; j < mall.size(); ++j) {
            double val = mall.get(j).getglycemy();
            float f = (float) val;
            values.add(new PointValue(j, f));
            String day = mall.get(j).getDateCreation().toString().substring(3, 5);
            String month = mall.get(j).getDateCreation().toString().substring(0, 2);
            String formated_date = day + "-" + month;
            axisValues.add(new AxisValue(j).setLabel(formated_date));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        lines.add(line);

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Jours");
                axisY.setName("Glycémie");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            if (mall.size() == 1)
                pointIndex--;
            final EntryOfCDS entry = mall.get(pointIndex);
            GoToEntry go = new GoToEntry(entry, getContext(), getActivity());
        }

        @Override
        public void onValueDeselected() {

        }

    }
}