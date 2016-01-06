package fr.diabhelp.carnetdesuivi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import fr.diabhelp.carnetdesuivi.Carnet.CarnetActivity;

public class Carnetdesuivi extends AppCompatActivity {

    public GridView grid;
    private String[] web = {
            "Carnet de suivi",
            "Statistiques",
            "Export PDF",
            "Paramètre"

    } ;

    public int[] imageId = {
            R.drawable.diab_logo,
            R.drawable.diab_logo,
            R.drawable.diab_logo,
            R.drawable.diab_logo,
            R.drawable.diab_logo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CustomGridMain adapter = new CustomGridMain(Carnetdesuivi.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    Intent carnetintent = new Intent(Carnetdesuivi.this, CarnetActivity.class);
                    Carnetdesuivi.this.startActivity(carnetintent);
                }
                else if (position == 1) // statistiques
                {
                    Toast.makeText(Carnetdesuivi.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
                }
                else if (position == 2) // Export
                {
                    Toast.makeText(Carnetdesuivi.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
                }
                else if (position == 1) // Parametre
                {
                    Toast.makeText(Carnetdesuivi.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
