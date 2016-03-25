package fr.diabhelp.diabhelp.Models;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by Simon for Diabhelp
 * Started on 12 Oct 2015 at 14:46
 */
public class CatalogModule {
    private String          _name;
    private String          _version;
    private String          _rating;
    private String          _desc;
    private String          _URLStore;
    private String          _URLWeb;
    private Drawable        _logo;
    private String          _size;
    private Boolean         _new;
    private String          maker;
    private List<String>    _commentaires;

    //default constructor
    public CatalogModule() {}

    public String getMaker() {return this.maker;}

    public void setMaker(String maker) {this.maker = maker;}

    public List<String> getCommentaires() {return this._commentaires;}

    public void setCommentaires(List<String> _commentaires) {this._commentaires = _commentaires;}

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
