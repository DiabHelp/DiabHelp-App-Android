package fr.diabhelp.carnetdesuivi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;

public class testEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_entry);

        List<EntryOfCDS> listEntries = new ArrayList<EntryOfCDS>();

        //listEntries.add();

    }
}
