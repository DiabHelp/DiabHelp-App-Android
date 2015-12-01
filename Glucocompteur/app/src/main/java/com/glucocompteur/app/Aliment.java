package com.glucocompteur.app;

import android.graphics.drawable.Drawable;

/**
 * Created by Simon on 23-Nov-15.
 */
public class Aliment {
    private String      _name;
    private Integer     _weight;
    private Integer     _glucids;
    private Integer     _totalGlucids;
    private Drawable    _alimentLogo;

    public Aliment(String name, Integer weight, Integer glucids, Integer totalGlucids) {
        _name = name;
        _weight = weight;
        _glucids = glucids;
        _totalGlucids = totalGlucids;
    }

    public String getName() { return _name; }

    public void setName(String name) { _name = name; }

    public Integer getWeight() { return _weight; }

    public void setWeight(Integer weight) { _weight = weight; }

    public Integer getGlucids() { return _glucids; }

    public void set_glucids(Integer glucids) { _glucids = glucids; }

    public Integer getTotalGlucids() { return _totalGlucids; }

    public void setTotalGlucids(Integer totalGlucids) { _totalGlucids = totalGlucids; }

    public Drawable getAlimentLogo() { return _alimentLogo; }

    public void setAlimentLogo(Drawable alimentLogo) { this._alimentLogo = alimentLogo; }

}
