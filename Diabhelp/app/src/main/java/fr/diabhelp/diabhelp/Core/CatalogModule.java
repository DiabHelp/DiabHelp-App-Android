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
    private String      _URL;
    private Drawable    _logo;
    private String      _size;

    public CatalogModule(String _desc, Drawable _logo, String _name, String _rating, String _size, String _version) {}
    public CatalogModule() {}

    public String getDesc() { return _desc; }

    public void setDesc(String _desc) {
        this._desc = _desc;
    }

    public Drawable getLogo() {
        return _logo;
    }

    public void setLogo(Drawable _logo) {
        this._logo = _logo;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getRating() {
        return _rating;
    }

    public void setRating(String _rating) {
        this._rating = _rating;
    }

    public String getVersion() { return _version; }

    public void setVersion(String _version) { this._version = _version; }

    public String getURL() { return _URL; }

    public void setURL(String _URL) { this._URL = _URL; }


    public String getSize() { return _size; }

    public void setSize(String _size) { this._size = _size; }


}
