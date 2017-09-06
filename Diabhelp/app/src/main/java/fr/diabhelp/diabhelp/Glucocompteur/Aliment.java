package fr.diabhelp.diabhelp.Glucocompteur;

import android.graphics.drawable.Drawable;

/**
 * Created by Simon on 23-Nov-15.
 */
public class Aliment {
    private String      _name;
    private Double      _weight;
    private Double      _glucids; // g/100g
    private Double      _totalGlucids;
    private Drawable    _alimentLogo;

    public Aliment(String name, Double weight, Double glucids) {
        _name = name;
        _weight = weight;
        _glucids = glucids;
        _totalGlucids = (weight * glucids) / 100;
    }

    public String getName() { return _name; }

    public void setName(String name) { _name = name; }

    public Double getWeight() { return _weight; }

    public void setWeight(Double weight) { _weight = weight; }

    public Double getGlucids() { return _glucids; }

    public void set_glucids(Double glucids) { _glucids = glucids; }

    public Double getTotalGlucids() { return _totalGlucids; }

    public void setTotalGlucids(Double totalGlucids) { _totalGlucids = totalGlucids; }

    public Drawable getAlimentLogo() { return _alimentLogo; }

    public void setAlimentLogo(Drawable alimentLogo) { this._alimentLogo = alimentLogo; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Aliment :");
        sb.append(System.getProperty("line.separator"));
        sb.append("name : " + _name);
        sb.append(System.getProperty("line.separator"));
        sb.append("weight : " + _weight);
        sb.append(System.getProperty("line.separator"));
        sb.append("glucides : " + _glucids);
        sb.append(System.getProperty("line.separator"));
        sb.append("add_favorites glucides : " + _totalGlucids);
        sb.append(System.getProperty("line.separator"));
        return (sb.toString());
    }
}
