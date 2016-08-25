package fr.diabhelp.proche;

/**
 * Created by 4kito on 02/08/2016.
 */
public class PatientInfo {
    private String  _name;
    private int     _id;
    private int     _state;

    public PatientInfo(String name, int id, int state) {
        _name = name;
        _id = id;
        _state = state;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getState() {
        return _state;
    }

    public void setState(int state) {
        _state = state;
    }
}
