package fr.diabhelp.diabhelp.Glucocompteur;

import java.util.ArrayList;

/**
 * Created by Simon on 05-Feb-16.
 */
public class MyMenu {
    public String               menuName;
    public Double               menuGlucids;
    public ArrayList<Aliment>   alimentsList;

    public MyMenu() {
        alimentsList = new ArrayList<>();
    }

    public MyMenu(String name, ArrayList<Aliment> aliments) {
        menuName = name;
        alimentsList = aliments;
        menuGlucids = calcGlucids(alimentsList);
    }

    private Double calcGlucids(ArrayList<Aliment> alimentsList) {
        Double tmp = 0.;
        for (Aliment aliment : alimentsList) {
            tmp += aliment.getTotalGlucids();
        }
        return (tmp);
    }

    public String getMenuName() {return menuName;}

    public Double getMenuGlucids() {return menuGlucids;}

    public ArrayList<Aliment> getAlimentsList() { return alimentsList;}

    public void setMenuName (String name)
    {
        this.menuName = name;
    }

    public void setMenuGlucids(Double glucids)
    {
        this.menuGlucids = glucids;
    }

    public void addAliment(Aliment aliment)
    {
        alimentsList.add(aliment);
    }

    public void removeAliment(Aliment aliment)
    {
        alimentsList.remove(aliment);
    }



}
