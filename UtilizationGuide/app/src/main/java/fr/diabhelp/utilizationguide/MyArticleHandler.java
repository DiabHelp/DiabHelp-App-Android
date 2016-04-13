package fr.diabhelp.utilizationguide;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maxime on 29/03/2016.
 */
public class MyArticleHandler {
//    private String title;
    private static List<Article> articles;
    private static Iterator<Article> iterator;
    private Context context;
    MyArticleHandler(Context pContext){
        context = pContext;
        initializeData();
    }

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(){
        articles = new ArrayList<>();
        articles.add(new Article("Premiers pas dans l'application", "Accueil", context.getString(R.string.article_accueil), R.mipmap.ic_launcher));
        articles.add(new Article("Premiers pas dans l'application", "AccueilAutre", context.getString(R.string.article_accueil2), R.mipmap.ic_launcher));
        articles.add(new Article("Gestion des modules - Le catalogue", "Premiers pas", context.getString(R.string.catalogue_premiers_pas), R.mipmap.ic_launcher));
        articles.add(new Article("Gestion des modules - Le catalogue", "Installation des modules", context.getString(R.string.catalogue_installation), R.mipmap.ic_launcher));
        articles.add(new Article("Gestion des modules - Le catalogue", "Mise à jour des modules", context.getString(R.string.catalogue_mise_a_jour), R.mipmap.ic_launcher));
        articles.add(new Article("Connexion, inscription", "Comment se connecter", context.getString(R.string.connexion), R.mipmap.ic_launcher));
        articles.add(new Article("Connexion, inscription", "Inviter un proche à créer un compte", context.getString(R.string.inviter_proche), R.mipmap.ic_launcher));
        articles.add(new Article("Les différents modules disponibles", "Le carnet de suivi", context.getString(R.string.carnet_de_suivi), R.mipmap.ic_launcher));
        articles.add(new Article("Les différents modules disponibles", "Le glucocompteur", context.getString(R.string.glucocompteur), R.mipmap.ic_launcher));
        articles.add(new Article("Les différents modules disponibles", "Le suivi par un proche", context.getString(R.string.suivi_par_proche), R.mipmap.ic_launcher));
        articles.add(new Article("Les différents modules disponibles", "La relation médecin-patient", context.getString(R.string.relation_medecin_patient), R.mipmap.ic_launcher));
        articles.add(new Article("Les différents modules disponibles", "Les alertes glycémiques", context.getString(R.string.alertes_glycemiques), R.mipmap.ic_launcher));
        articles.add(new Article("Les différents modules disponibles", "La Foire aux Questions", context.getString(R.string.foire_aux_questions), R.mipmap.ic_launcher));
    }

    public static boolean isCategory(String category){
        if (Arrays.asList(getCategories()).contains(category))
            return true;
        return false;
    }

    public static boolean isRubrik(String rubrik){
        if (Arrays.asList(getAllTitles()).contains(rubrik))
            return true;
        return false;
    }

    public static String[] getDistinctCategories(){
        List<String> existingKey = new ArrayList<String>();
        String[] categories = getCategories();
        int i = 0;
        while (i < categories.length){
            if (!(existingKey.contains(categories[i])))
                existingKey.add(categories[i]);
            i++;
        }
        return (existingKey.toArray(new String[0]));
    }

    public static String[] getCategories(){
        List<String> categories = new ArrayList<String>();
        if (articles.size() == 0)
            return null;
        else if (articles != null && articles.size() > 0){
            for (iterator = articles.iterator(); iterator.hasNext();){
                Article item = iterator.next();
                categories.add(item.category);
            }
        }
        return categories.toArray(new String[0]);
    }

    public static Article getArticle(String title, String category) //carefull, title must be unique in the category
    {
        Article ret = null;
        for (iterator = articles.iterator(); iterator.hasNext();){
            ret = iterator.next();
//            Log.e("processing", "analyzing element " + item.title + " in category " + item.category);
            if (ret.title.equals(title) && ret.category.equals(category))
                return ret;
        }
        return ret;
    }

    public static String[] getArticlesTitles(String category)
    {
        List<String> titles = new ArrayList<String>();
        for (iterator = articles.iterator(); iterator.hasNext();){
            Article item = iterator.next();
            if (item.category.equals(category))
                titles.add(item.getTitle());
        }
        return titles.toArray(new String[0]);
    }

    public static String[] getAllTitles() {
        List<String> titles = new ArrayList<String>();
        for (iterator = articles.iterator(); iterator.hasNext();){
            Article item = iterator.next();
            titles.add(item.getTitle());
        }
        return titles.toArray(new String[0]);
    }
}
