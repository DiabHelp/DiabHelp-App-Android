package fr.diabhelp.proche;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by 4kito on 02/08/2016.
 */
public class SuivisFragment extends Fragment {
    private RecyclerView                    _recyclerView;
    private RecyclerView.Adapter            _recAdapter;
    private RecyclerView.LayoutManager      _recLayoutManager;
    private ArrayList<Patient>              _patientsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_suivis, container, false);
        _recyclerView = (RecyclerView) v.findViewById(R.id.patient_suivi_recycler_view);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        _patientsList.add(new Patient("Paul", "OCHON", null, Patient.State.OK));
        _patientsList.add(new Patient("Anne", "ONYME", null, Patient.State.ALERT));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _patientsList.add(new Patient("Jean", "BONO", null, Patient.State.DANGER));
        _recAdapter = new PatientRecyclerAdapter(getContext(), this, _patientsList);
        _recyclerView.setAdapter(_recAdapter);
        return v;
    }

}

