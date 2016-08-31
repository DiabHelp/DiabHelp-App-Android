package fr.diabhelp.proche.Utils;

import android.content.Context;

/**
 * Created by Sumbers on 06/07/2016.
 */
public class SharedContext {

    private static Context sharedContext;

    public static void setContext(Context context){SharedContext.sharedContext = context;}

    public static Context getSharedContext() {return SharedContext.sharedContext;}
}
