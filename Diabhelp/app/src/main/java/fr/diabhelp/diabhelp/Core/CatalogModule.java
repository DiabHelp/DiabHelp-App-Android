package fr.diabhelp.diabhelp.Core;

import android.graphics.drawable.Drawable;

/**
 * Created by Simon for Diabhelp
 * Started on 12 Oct 2015 at 14:46
 */
public class CatalogModule {
    private String      _name;
    private String      _version;
    private String      _rating;
    private String      _desc;
    private String      _URLStore;
    private String      _URLWeb;
    private Drawable    _logo;
    private String      _size;
    private Boolean     _new;

    public CatalogModule(String _desc, Drawable _logo, String _name, String _rating, String _size, String _version) {
        this._desc = _desc;
        this._logo = _logo;
        this._name = _name;
        this._rating = _rating;
        this._size = _size;
        this._version = _version;
    }

    public CatalogModule() {}

    public String getDesc() { return _desc; }

    public void setDesc(String desc) {
        _desc = desc;
    }

    public Drawable getLogo() {
        return _logo;
    }

    public void setLogo(Drawable logo) {
        _logo = logo;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getRating() {
        return _rating;
    }

    public void setRating(String rating) {
        _rating = rating;
    }

    public String getVersion() { return _version; }

    public void setVersion(String version) { _version = version; }

    public String getURLStore() { return _URLStore; }

    public void setURLStore(String URLStore) { _URLStore = URLStore; }

    public String getURLWeb() { return _URLWeb; }

    public void setURLWeb(String URLWeb) { _URLWeb = URLWeb; }

    public String getSize() { return _size; }

    public void setSize(String size) { _size = size; }

    public Boolean getNew() { return _new; }

    public void setNew(Boolean bNew) { _new = bNew; }

}
