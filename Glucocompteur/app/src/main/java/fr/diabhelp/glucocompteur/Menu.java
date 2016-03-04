package fr.diabhelp.glucocompteur;

import java.util.ArrayList;

/**
 * Created by Simon on 05-Feb-16.
 */
public class Menu {
    public String               menuName;
    public int                  menuGlucids;
    public ArrayList<Aliment>   alimentsList;

    public String getMenuName() {return menuName;}
    public int getMenuGlucids() {return menuGlucids;}
    public ArrayList<Aliment> getAlimentsList() { return alimentsList;}

    public void setMenuName (String name)
    {
        this.menuName = name;
    }

    public void setMenuGlucids(int glucids)
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
