package fr.diabhelp.proche.Utils;

/**
 * Created by Sumbers on 07/09/2016.
 */
public enum ManageListStatus {
    WAITING,
    ACCEPTED,
    REJECTED,
    DELETED;

    public static ManageListStatus getById(int id) {
        for(ManageListStatus e : values()) {
            if(e.ordinal() == (id)) return e;
        }
        return null;
    }
}
