package com.glucocompteur.app;

import android.graphics.drawable.Drawable;

/**
 * Created by Simon on 23-Nov-15.
 */
public class Aliment {
    private String      _name;
    private Float     _weight;
    private Float     _glucids;
    private Float     _totalGlucids;
    private Drawable    _alimentLogo;

    public Aliment(String name, Float weight, Float glucids, Float totalGlucids) {
        _name = name;
        _weight = weight;
        _glucids = glucids;
        _totalGlucids = totalGlucids;
    }

    public String getName() { return _name; }

    public void setName(String name) { _name = name; }

    public Float getWeight() { return _weight; }

    public void setWeight(Float weight) { _weight = weight; }

    public Float getGlucids() { return _glucids; }

    public void set_glucids(Float glucids) { _glucids = glucids; }

    public Float getTotalGlucids() { return _totalGlucids; }

    public void setTotalGlucids(Float totalGlucids) { _totalGlucids = totalGlucids; }

    public Drawable getAlimentLogo() { return _alimentLogo; }

    public void setAlimentLogo(Drawable alimentLogo) { this._alimentLogo = alimentLogo; }

}