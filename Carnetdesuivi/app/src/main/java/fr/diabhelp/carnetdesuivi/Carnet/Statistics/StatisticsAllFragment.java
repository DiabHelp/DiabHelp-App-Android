package fr.diabhelp.carnetdesuivi.Carnet.Statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.carnetdesuivi.DataBase.DAO;
import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
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

public class StatisticsAllFragment extends Fragment {

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    private ArrayList<EntryOfCDS> mall = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.statistics_all_fragment, container, false);

        chart = (ColumnChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        getData();

        return rootView;
    }

    private void getData() {
        DAO bdd = new DAO(getContext());
        bdd.open();
        mall = bdd.SelectAll();
        bdd.close();

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
//            Toast.makeText(getActivity(), "Glycémie: " + value.getValue(), Toast.LENGTH_SHORT).show();
            final EntryOfCDS entry = mall.get(subcolumnIndex);
            GoToEntry go = new GoToEntry(entry, getContext(), getActivity());
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

}
