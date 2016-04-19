package fr.diabhelp.medecin_patient;

/**
 * Created by sundava on 22/03/16.
 */
public class MedecinInfo {
    private String  name;
    private int  id;
    private int     state;
    public MedecinInfo(String name, int id, int state) {
        this.name = name;
        this.id = id;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
