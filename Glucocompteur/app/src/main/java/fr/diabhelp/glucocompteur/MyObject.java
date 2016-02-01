package fr.diabhelp.glucocompteur;

/**
 * Created by Kataleya on 14/03/2015.
 */

public class MyObject {
    public String objectName;
    public String objectGlucides;
    // constructor for adding sample data
    public MyObject(String objectName, String objectGlucides){
        this.objectName = objectName;
        this.objectGlucides = objectGlucides;
    }

    public String getObjectGlucides() {
        return this.objectGlucides;
    }
    public String getObjectName() {
        return this.objectName;
    }
}