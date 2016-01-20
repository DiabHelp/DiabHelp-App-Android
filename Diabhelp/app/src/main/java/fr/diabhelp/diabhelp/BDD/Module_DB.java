package fr.diabhelp.diabhelp.BDD;

/**
 * Created by naqued on 28/09/15.
 */
public class Module_DB {
    // Notez que l'identifiant est un long
    private long id;
    private String appname;
    private String pname;
    private String version;
    private String last_update;


    public Module_DB() {
        super();
    }

    public Module_DB(String name) {
        super();
        this.pname = name;
    }

    public long getId() { return this.id; }
    public String getAppname() { return this.appname; }
    public String getPname() { return this.pname; }
    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLast_update() {
        return last_update;
    }

    public String getVersion() {
        return version;
    }
}
