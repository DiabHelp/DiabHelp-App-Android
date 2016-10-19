package fr.diabhelp.medecin_patient;

/**
 * Created by sundava on 22/03/16.
 */
public class MedecinInfo {
    private String  name;
    private int  id;
    private State     state;
    public MedecinInfo(String name, int id, State state) {
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State
    {
        PRESENT,
        PROCESSING,
        REMOVED;

        public static State getById(int id) {
            for(State e : values()) {
                if(e.ordinal() == (id)) return e;
            }
            return null;
        }
    }
}
