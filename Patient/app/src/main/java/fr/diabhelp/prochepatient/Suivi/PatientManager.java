package fr.diabhelp.prochepatient.Suivi;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 4kito on 07/11/2016.
 */
public class PatientManager {
    Activity                                        _parent;
    String                                          _idUser;
    private ArrayList<ExpandableListAdapter.Item>   _patientsItems = new ArrayList<>();

    public PatientManager(Activity parent, String idUser) {
        Log.d("PROUT", idUser);
        _parent = parent;
        _idUser = idUser;
    }

//    public ArrayList<ExpandableListAdapter.Item> getUserItems() {
//        ExpandableListAdapter.Item patient = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 2, "Paul", "OCHON", new Location(""), null);
//        patient.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.PHONE, null, null, null, "0632524154"));
//        patient.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.ALERT, null, null, null, null));
//        _patientsItems.add(patient);
//        ExpandableListAdapter.Item patient1 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 2, "Anne", "ONYME", new Location(""), null);
//        patient1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.PHONE, null, null, null, "0632524154"));
//        patient1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.ALERT, null, null, null, null));
//        _patientsItems.add(patient1);
//        ExpandableListAdapter.Item patient2 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 2, "Jean", "BONO", new Location(""), null);
//        patient2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.PHONE, null, null, null, "0632524154"));
//        patient2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.ALERT, null, null, null, null));
//        _patientsItems.add(patient2);
//        return _patientsItems;
}
