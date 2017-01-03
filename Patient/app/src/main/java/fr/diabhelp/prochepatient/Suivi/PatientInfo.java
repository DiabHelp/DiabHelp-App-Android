package fr.diabhelp.prochepatient.Suivi;

/**
 * Created by 4kito on 19/10/2016.
 */
public class PatientInfo {
    private String  _phone;
    public Type     _type;

    public enum Type {
        PHONE, ALERT
    }

    public PatientInfo(String phone, Type type) {
        _phone = phone;
        _type = type;
    }

    public String getPhone() {
        return _phone;
    }

    public Type getType() { return _type; }
}
