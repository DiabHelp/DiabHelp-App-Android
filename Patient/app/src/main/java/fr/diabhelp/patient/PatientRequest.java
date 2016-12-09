package fr.diabhelp.patient;

/**
 * Created by 4kito on 02/08/2016.
 */
public class PatientRequest {
    private String name;
    private String id;
    private State state;

    public PatientRequest(String name, String id, State state) {
        this.name = name;
        this.id = id;
        this.state = state;
    }

    public PatientRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        WAITING,
        PROCESSING,
        ACCEPTED,
        REFUSED;

        public static State getById(int id) {
            for(State e : values()) {
                if(e.ordinal() == (id)) return e;
            }
            return null;
        }
    }
}
