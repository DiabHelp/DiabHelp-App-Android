package fr.diabhelp.glucocompteur;

import java.util.ArrayList;

/**
 * Created by Simon on 05-Feb-16.
 */
public class Menu {
    public String               menuName;
    public Double               menuGlucids;
    public ArrayList<Aliment>   alimentsList;

    public Menu() {
        alimentsList = new ArrayList<>();
    }

    public Menu (String name, ArrayList<Aliment> aliments) {
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
