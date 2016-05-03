package fr.diabhelp.glucocompteur;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by Simon on 25-Nov-15.
 */
public class CurrentMenuFragment extends Fragment {
    private RecyclerView                _recyclerView;
    private RecyclerView.Adapter        _recAdapter;
    private RecyclerView.LayoutManager  _recLayoutManager;
    private ArrayList<Aliment>          _alimentsList = new ArrayList<>();
    private Aliment                     _aliment;
    private DBSearchBox                 _searchBox;
    private DBHelper                    _dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        _dbHelper = new DBHelper(getActivity());
        if (_dbHelper.openDataBase() == false)
            Log.d("DBAliment", "Could not open DB");
        _recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        _recAdapter = new AlimentRecyclerAdapter(_alimentsList);
        _recyclerView.setAdapter(_recAdapter);
        v.findViewById(R.id.addFood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.add_aliment);
                _searchBox = (DBSearchBox) dialog.findViewById(R.id.search_input);
                _searchBox.initDBHooks(_dbHelper);
                _searchBox.setAlimentList(_alimentsList);
                _searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MyObject selected = _searchBox.products.get(0);
                        String glucidesStr = selected.getObjectGlucides().replace(",", ".");
                        double glucides = 0.;
                        try {
                            glucides = Double.parseDouble(glucidesStr);

                        } catch (NumberFormatException e) {
                            Log.d("DBField", "Incorrect field : " + selected.getObjectName() + " : " + selected.getObjectGlucides());
                        }
                        _aliment = new Aliment(selected.getObjectName(), 0., glucides);
                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double weight;
                        EditText pEdit = (EditText) dialog.findViewById(R.id.quantite);
                        if (_searchBox.getText().toString().matches("") || pEdit.getText().toString().matches("")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            if (_searchBox.getText().toString().matches("")) {
                                alertDialogBuilder.setTitle("Aliment non renseigné");
                                alertDialogBuilder.setMessage("Veuillez choisir un aliment")
                                        .setCancelable(true);
                            } else {
                                alertDialogBuilder.setTitle("Quantité non renseigné");
                                alertDialogBuilder.setMessage("Veuillez renseigner la quantité de l'aliment choisi")
                                        .setCancelable(true);
                            }
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            weight = Double.parseDouble(String.valueOf(pEdit.getText()));
                            _aliment.setWeight(weight);
                            _aliment.setTotalGlucids(weight * _aliment.getGlucids());
                            _alimentsList.add(_aliment);
                            _searchBox.setText("");
                            TextView totalGlucids  = (TextView) container.getRootView().findViewById(R.id.totalGlucids);
                            TextView totalWeight  = (TextView) container.getRootView().findViewById(R.id.totalWeight);
                            totalGlucids.setText("Glycemie totale : " + getTotalGlucids(_alimentsList).toString() + "g");
                            totalWeight.setText("Poids total : " + String.valueOf(getTotalWeight(_alimentsList)) + "g");
                            _recAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setTitle("Ajouter un aliment");
                dialog.show();
            }
        });
        v.findViewById(R.id.addFavorites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_alimentsList.isEmpty()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Aucun aliment");
                    alertDialogBuilder.setMessage("Veuillez ajouter un aliment à votre menu")
                            .setCancelable(true);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.add_favorite);
                    Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText nEdit = (EditText) dialog.findViewById(R.id.nom);
                            if (nEdit.getText().toString().matches("")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setTitle("Nom non renseigné");
                                alertDialogBuilder.setMessage("Veuillez donner un nom à votre menu")
                                        .setCancelable(true);
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else {
                                MenuManager manager = new MenuManager(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
                                ArrayList<Menu> menus = manager.getSavedMenu();
                                Menu menu = new Menu(nEdit.getText().toString(), _alimentsList);
                                menus.add(menu);
                                JSONMenuWriter writer = new JSONMenuWriter(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
                                writer.saveMenu(menus);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setTitle("Ajouter un menu favori");
                    dialog.show();
                }
            }
        });
        return v;
    }

    private Double getTotalWeight(ArrayList<Aliment> aliments) {
        Double  res = 0.;

        for (Aliment aliment : aliments) {
            res += aliment.getWeight();
        }
        return res;
    }

    private Double getTotalGlucids(ArrayList<Aliment> aliments) {
        Double res = 0.;

        for (Aliment aliment : aliments) {
            res += aliment.getTotalGlucids();
        }
        return res;
    }

}
