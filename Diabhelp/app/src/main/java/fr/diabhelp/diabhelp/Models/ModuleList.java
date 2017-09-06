package fr.diabhelp.diabhelp.Models;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.Core.ParametresModule;
import fr.diabhelp.diabhelp.R;

/**
 * Created by Sumbers on 21/07/2016.
 */
public class ModuleList {

    private final Context context;

    private ArrayList<ModuleList.PInfo> apps;
    private final ArrayList<String> listApp = new ArrayList<String>();
    private String[] web;
    private Drawable[] img;

    public class PInfo{
        public String appname = "";
        public String pname = "";
        public String versionName = "";
        public int versionCode;
        public Drawable icon;
        public String publicSourceDir;

        private void DebugPrint() {
            Log.v(this.appname, this.appname + "\t" + this.pname + "\t" + this.versionName + "\t" + this.versionCode);
        }
    }

    public ModuleList(Context context) {
        this.context = context;
        apps = this.getModule();

    }
    public ArrayList<ModuleList.PInfo> getModule()
    {
        this.apps = new ArrayList<PInfo>();
        ModuleList.PInfo cds = new ModuleList.PInfo();
        ModuleList.PInfo glu = new ModuleList.PInfo();;
        cds.appname = "Carnet de suivi";
        cds.pname = "Carnet de suivi";
        this.web = new String[2];
        this.img = new Drawable[2];
        this.web[0] = "Carnet de suivi";
        this.img[0]  = context.getResources().getDrawable( R.drawable.old_ic_launcher );
        this.web[1] = "Glucocompteur";
        this.img[1]  = context.getResources().getDrawable( R.drawable.ic_launcher_gl );
        cds.publicSourceDir = "https://diabhelp.fr/fr/modules/21";
        glu.appname = "Glucocompteur";
        glu.pname = "Glucocompteur";
        glu.publicSourceDir = "https://diabhelp.fr/fr/modules/17";
        this.apps.add(0,cds);
        this.apps.add(1, glu);
        return this.apps;

    }

    public void update()
    {
        this.apps = getModule();
    }

    public ArrayList<ModuleList.PInfo> getPackages() {
        ArrayList<ModuleList.PInfo> apps = this.getInstalledApps(false); /* false = no system packages */
        int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).DebugPrint();
        }
        return apps;
    }

    public ArrayList<ParametresModule> getModulesList() {
        ArrayList<ModuleList.PInfo> pInfoList= getAppList();
        Log.d("ModuleManager", "pInfoList CoreActivity size = " + pInfoList.size());
        ArrayList modulesList = new ArrayList<>();
        if (pInfoList != null)
        {
            int moduleListSize = pInfoList.size(); //Offset parce que l'array actuel contient l'aide + la redirection vers le site
            for (int i = 0; i < moduleListSize; i++)
            {
                ModuleList.PInfo module = pInfoList.get(i);
                long size = new File(module.publicSourceDir).length();
                String sizeStr = String.format("%.2f", size / 1000000.0) + " Mo";
                modulesList.add(new ParametresModule(module.icon, module.appname, module.pname, sizeStr, "Version " + module.versionName));
            }
        }
        return modulesList;
    }

    public ArrayList<ModuleList.PInfo> getAppList() { return apps; }

    private ArrayList<ModuleList.PInfo> getInstalledApps(boolean getSysPackages) {
        int ctr = 0;

        ModuleList.PInfo newInfo;

        ArrayList<ModuleList.PInfo> res = new ArrayList<ModuleList.PInfo>();
        List<PackageInfo> packs = this.context.getPackageManager().getInstalledPackages(0);
        int nbDApp = this.countApp(packs);
        this.img = new Drawable[nbDApp];
        this.web = new String[nbDApp];
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);

            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp")) {

                if (!getSysPackages && p.versionName == null) {
                    continue;
                }
                newInfo = new ModuleList.PInfo();
                newInfo.appname = p.applicationInfo.loadLabel(this.context.getPackageManager()).toString();
                newInfo.pname = p.packageName;
                this.listApp.add(p.packageName);
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                newInfo.publicSourceDir = p.applicationInfo.publicSourceDir;
                newInfo.icon = p.applicationInfo.loadIcon(this.context.getPackageManager()); // choper le drawable
                if (newInfo.icon != null && newInfo.appname != null) {
                    this.img[ctr] = newInfo.icon;
                    this.web[ctr++] = newInfo.appname;
                }
                res.add(newInfo);
            }
        }
        return res;
    }

    public String[] getAppStringList() { return web; }
    public Drawable[] getAppDrawableList() { return img; }

    private int countApp(List<PackageInfo> packs) {
        int ctr = 0;

        /*for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp"))
                ctr++;
        }*/

        return ctr;
    }

}
