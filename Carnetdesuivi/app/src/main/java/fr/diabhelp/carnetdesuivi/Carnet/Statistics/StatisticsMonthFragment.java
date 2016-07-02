package fr.diabhelp.carnetdesuivi.Carnet.Statistics;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import fr.diabhelp.carnetdesuivi.R;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by vigour_a on 02/02/2016.
 */

public class StatisticsMonthFragment extends Fragment {

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;
    private DAO dao = null;
    private SQLiteDatabase db = null;

    private ArrayList<EntryOfCDS> mall = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistics_month_fragment, container, false);

        chart = (ColumnChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        getData();

        chart.setViewportCalculationEnabled(false);

        return rootView;
    }

    private void getData() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        String endDate = sdf.format(c.getTime());

        c.add(Calendar.DATE, -30);
        String startDate = sdf.format(c.getTime());


        mall = EntryOfCDSDAO.selectBetweenDays(startDate, endDate, db);

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
//        List<AxisValue> axisValues = new ArrayList<>();

        values = new ArrayList<SubcolumnValue>();

        for (int j = 0; j < mall.size(); ++j) {
            double val = mall.get(j).getglycemy();
            float f = (float) val;
            if (val > 125 || val < 75)
                values.add(new SubcolumnValue(f, Color.parseColor("#FF4444")));
            else
                values.add(new SubcolumnValue(f, Color.parseColor("#99CC00")));
//            String day = mall.get(j).getDate().toString().substring(3, 5);
//            String month = mall.get(j).getDate().toString().substring(0, 2);
//            String formated_date = day + "-" + month;
//            axisValues.add(new AxisValue(j).setLabel(formated_date));
        }

        Column column = new Column(values);
        column.setHasLabels(hasLabels);
        column.setHasLabelsOnlyForSelected(hasLabelForSelected);
        columns.add(column);

        data = new ColumnChartData(columns);

        if (hasAxes) {
//            Axis axisX = new Axis(axisValues);
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Jours");
                axisY.setName("Glycémie");
            }
//            data.setAxisXBottom(axisX);
            data.setAxisXBottom(null);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            final EntryOfCDS entry = mall.get(subcolumnIndex);
            GoToEntry go = new GoToEntry(entry, getContext(), getActivity());
        }

        @Override
        public void onValueDeselected() {

        }

    }

}
