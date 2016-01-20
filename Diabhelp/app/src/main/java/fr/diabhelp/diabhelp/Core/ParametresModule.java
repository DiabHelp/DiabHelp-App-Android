package fr.diabhelp.diabhelp.Core;

import android.graphics.drawable.Drawable;

/**
 * Created by Simon for Diabhelp
 * Started on 12 Oct 2015 at 14:46
 */
public class ParametresModule {
    private String _latestVersion;
    private String      _name;
    private String      _version;
    private String      _appname;
    private Drawable    _logo;
    private String      _size;

    public ParametresModule(Drawable _logo, String _name, String _appname, String _size, String _version) {
        this._logo = _logo;
        this._name = _name;
        this._appname = _appname;
        this._size = _size;
        this._version = _version;
        this._latestVersion = null;
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

    public String getAppName() {
        return _appname;
    }

    public void setAppName(String _appname) {
        this._appname = _appname;
    }

    public String getVersion() { return _version; }

    public void setVersion(String _version) { this._version = _version; }

    public String getSize() { return _size; }

    public void setSize(String _size) { this._size = _size; }


    public String getLatestVersion() {
        return _latestVersion;
    }

    public void setLatestVersion(String _latestVersion) {
        this._latestVersion = _latestVersion;
    }
}
