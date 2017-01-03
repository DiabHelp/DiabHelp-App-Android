package fr.diabhelp.prochepatient.Suivi;

import android.location.Location;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import fr.diabhelp.prochepatient.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4kito on 19/10/2016.
 */
public class PatientParent implements Parent<PatientInfo> {
    private String              _name;
    private String              _surname;
    private Location            _location;
    private User.State       _state;
    private List<PatientInfo>   _patientInfoList = new ArrayList<>();


    public  PatientParent(String name, String surname, Location location, User.State state, List<PatientInfo> patientInfoList) {
        _name = name;
        _surname = surname;
        _location = location;
        _state = state;
        _patientInfoList = patientInfoList;
    }

    public String getName() { return _name; }

    public String getSurname() { return _surname; }

    public Location getLocation() { return _location; }

    public User.State getState() { return _state; }

    @Override
    public List<PatientInfo> getChildList() {
        return _patientInfoList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public PatientInfo getPatientInfo(int childPosition) {
        return _patientInfoList.get(childPosition);
    }

}
