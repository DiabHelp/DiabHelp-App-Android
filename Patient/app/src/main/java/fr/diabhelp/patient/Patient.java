package fr.diabhelp.patient;

import android.location.Location;

/**
 * Created by 4kito on 03/08/2016.
 */
public class Patient {
    private String      id;
    private String      nom;
    private String      prenom;
    private String      mail;
    private String      tel;
    private String      photo;
    private Location    location;
    private State       state;

    public Patient(String firstname, String lastname, Location location, State ok) {
        prenom = firstname;
        nom = lastname;
        this.location = location;
        state = ok;
    }

    public enum State {
        OK,
        DANGER,
        ALERT
    }

    public Patient() {
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
