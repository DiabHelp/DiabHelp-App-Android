package fr.diabhelp.diabhelp.Core;

import android.graphics.drawable.Drawable;

/**
 * Created by Simon for Diabhelp
 * Started on 12 Oct 2015 at 14:46
 */
public class CatalogModule {
    private String      _name;
    private String      _size;
    private String      _version;
    private String      _rating;
    private String      _desc;
    private Drawable    _logo;

    public CatalogModule(String _desc, Drawable _logo, String _name, String _rating, String _size, String _version) {
        this._desc = _desc;
        this._logo = _logo;
        this._name = _name;
        this._rating = _rating;
        this._size = _size;
        this._version = _version;
    }

    public String getDesc() {

        return _desc;
    }

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

    public String getSize() {
        return _size;
    }

    public void setSize(String _size) {
        this._size = _size;
    }

    public String getVersion() {
        return _version;
    }

    public void setVersion(String _version) {
        this._version = _version;
    }
}
