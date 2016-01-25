package fr.diabhelp.carnetdesuivi;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;

/**
 * Created by Sumbers on 17/01/2016.
 */
public class CarnetSuiviTest extends ActivityInstrumentationTestCase2<Carnetdesuivi> {
    Context context;

    public CarnetSuiviTest() {
        super(Carnetdesuivi.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = getActivity();
        assertNotNull(context);
    }

    public void testDontWORK()
    {
        getA
        assertEquals(3, 2 + 2);
    }

    public void test(){
        Log.d("#test", "test");
    }

}
