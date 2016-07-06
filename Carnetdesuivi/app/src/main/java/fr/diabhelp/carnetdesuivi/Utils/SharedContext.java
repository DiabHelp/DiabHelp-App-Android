package fr.diabhelp.carnetdesuivi.Utils;

import android.content.Context;

/**
 * Created by Sumbers on 06/07/2016.
 */
public class SharedContext {

    private static Context sharedContext;
    private static Integer launch = 0;

    public static void setContext(Context context){SharedContext.sharedContext = context;}

    public static Context getSharedContext() {return SharedContext.sharedContext;}

    public static Integer getLaunch() {return SharedContext.launch;}

    public static void setLaunch(Integer launch) {SharedContext.launch = launch;}
}
