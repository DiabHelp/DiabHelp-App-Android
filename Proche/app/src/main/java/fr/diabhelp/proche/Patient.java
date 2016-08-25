package fr.diabhelp.proche;

import android.location.Location;

/**
 * Created by 4kito on 03/08/2016.
 */
public class Patient {
    private String      _name;
    private String      _surname;
    private Location    _gps;
    private State       _state;

    public enum State {
        OK,
        DANGER,
        ALERT
    }

    public Patient(final String name, final String surname, final Location gps, final State state) {
        _name = name;
        _surname = surname;
        _gps = gps;
        _state = state;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getSurname() {
        return _surname;
    }

    public void setSurname(String _surname) {
        this._surname = _surname;
    }

    public Location getGps() {
        return _gps;
    }

    public void setGps(Location _gps) {
        this._gps = _gps;
    }

    public State getState() {
        return _state;
    }

    public void setState(State _state) {
        this._state = _state;
    }

}
