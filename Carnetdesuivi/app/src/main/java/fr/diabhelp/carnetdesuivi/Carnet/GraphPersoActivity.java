package fr.diabhelp.carnetdesuivi.Carnet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.DataBase.DAO;
import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
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
public class GraphPersoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_statistics_perso_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistiques personnalisées");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Statisticsintent = new Intent(GraphPersoActivity.this, StatisticsActivity.class);
                startActivity(Statisticsintent);
            }
        });

        chart = (ColumnChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        getData();
    }

    private void getData() {

        DAO bdd = new DAO(this);
        bdd.open();
//        mall = bdd.selectBetweenDays("2016-02-01", "2016-02-25");
        mall = bdd.SelectAll();
        bdd.close();

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;

        values = new ArrayList<SubcolumnValue>();

        for (int j = 0; j < mall.size(); ++j) {
            double val = mall.get(j).getglycemy();
            float f = (float) val;
            if (val >= 8)
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
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);

        bdd.close();
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(GraphPersoActivity.this, "Glycémie moyenne de la journée: " + value.getValue(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

}
