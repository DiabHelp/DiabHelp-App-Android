package fr.diabhelp.suiviprochepatient.utils.BDD;

/**
 * Created by naqued on 28/09/15.
 */
public class User {
    // Notez que l'identifiant est un long
    private long id;
    private String user;
    private String pwd;

    public User(long id, String user, String pwd) {
        super();
        this.id = id;
        this.user = user;
        this.pwd = pwd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}