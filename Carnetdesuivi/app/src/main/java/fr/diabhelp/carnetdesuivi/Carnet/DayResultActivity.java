package fr.diabhelp.carnetdesuivi.Carnet;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import fr.diabhelp.carnetdesuivi.R;

public class DayResultActivity extends Activity {
    GridView grid;
    String[] web = {
    } ;
    int[] imageId = {
            R.drawable.round_cornerfast,
            R.drawable.round_cornerglu,
            R.drawable.round_cornerglyclean,
            R.drawable.round_cornerhba1c,
            R.drawable.round_cornerslow
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomGrid adapter = new CustomGrid(DayResultActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(DayResultActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

            }
        });

    }

}
