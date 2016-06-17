package fr.diabhelp.utilizationguide;

import android.content.Context;
import android.content.res.Resources;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import android.util.Log;

public class MyArticleHandler {
    private static MyArticleHandler instance;
    private static List<Article> articles;
    private Context context;
    private Resources res;

    public MyArticleHandler(Context pContext){
        context = pContext;
        res = pContext.getResources();
        initializeData();
    }

    //code to create the handler only one time
    public static synchronized MyArticleHandler getInstance(Context context){
        if (instance == null)
            instance = new MyArticleHandler(context);
        return instance;
    }

    // Here we fill the Articles of the User Manual that are composed
    // of ([String : Title], [String : Rubrik], [String-Array : content], [Int-Array : img-ids])
    // Articles Titles in each categories must be unique.
    private void initializeData(){
        articles = new ArrayList<>();
        //Premiers pas dans l'app
        articles.add(new Article(context.getString(R.string.ctg1), context.getString(R.string.ctg1_rbk1), res.getStringArray(R.array.ctg1_rbk1_art1)));
        articles.add(new Article(context.getString(R.string.ctg1), context.getString(R.string.ctg1_rbk2), res.getStringArray(R.array.ctg1_rbk2_art1)));
        articles.add(new Article(context.getString(R.string.ctg1), context.getString(R.string.ctg1_rbk3), res.getStringArray(R.array.ctg1_rbk3_art1)));
        articles.add(new Article(context.getString(R.string.ctg1), context.getString(R.string.ctg1_rbk4), res.getStringArray(R.array.ctg1_rbk4_art1)));

        //Modules DiabHelp
        articles.add(new Article(context.getString(R.string.ctg2), context.getString(R.string.ctg2_rbk1), res.getStringArray(R.array.ctg2_rbk1_art1)));
        articles.add(new Article(context.getString(R.string.ctg2), context.getString(R.string.ctg2_rbk2), res.getStringArray(R.array.ctg2_rbk2_art1)));
        articles.add(new Article(context.getString(R.string.ctg2), context.getString(R.string.ctg2_rbk3), res.getStringArray(R.array.ctg2_rbk3_art1)));
    }

    public static boolean isCategory(String category){
        return getCategories() != null && Arrays.asList(getCategories()).contains(category);
    }

    public static boolean isRubrik(String rubrik){
        return Arrays.asList(getAllTitles()).contains(rubrik);
    }

    public static String[] getDistinctCategories(){
        List<String> existingKey = new ArrayList<>();
        String[] categories = getCategories();
        int i = 0;
        assert categories != null;
        while (i < categories.length){
            if (!(existingKey.contains(categories[i])))
                existingKey.add(categories[i]);
            i++;
        }
        return (existingKey.toArray(new String[existingKey.size()]));
    }

    public static String[] getCategories(){
        List<String> categories = new ArrayList<>();
        Iterator<Article> iterator;
        if (articles != null && articles.size() == 0) return null;
        else if (articles != null && articles.size() > 0){
            for (iterator = articles.iterator(); iterator.hasNext();){
                Article item = iterator.next();
                categories.add(item.category);
            }
        }
        return categories.toArray(new String[categories.size()]);
    }


    public static Article getArticle(String title, String category) //carefull, title must be unique in the category
    {
        Article ret = null;
        Iterator<Article> iterator;
        for (iterator = articles.iterator(); iterator.hasNext();){
            ret = iterator.next();
            if (ret.title.equals(title) && ret.category.equals(category))
                return ret;
        }
        return ret;
    }

    //get all the articles titles of the category [category]
    public static String[] getArticlesTitles(String category){
        List<String> titles = new ArrayList<>();
        Iterator<Article> iterator;
        for (iterator = articles.iterator(); iterator.hasNext();){
            Article item = iterator.next();
            if (item.category.equals(category))
                titles.add(item.getTitle());
        }
        return titles.toArray(new String[titles.size()]);
    }

    //get all the articles titles
    public static String[] getAllTitles() {
        List<String> titles = new ArrayList<>();
        Iterator<Article> iterator;
        for (iterator = articles.iterator(); iterator.hasNext();){
            Article item = iterator.next();
            titles.add(item.getTitle());
        }
        return titles.toArray(new String[titles.size()]);
    }
}
