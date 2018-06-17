package com.spgrouptest.psidisplay;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.AndroidTestCase;

import com.spgrouptest.psidisplay.internal.PsiListener;
import com.spgrouptest.psidisplay.internal.PsiParser;

import org.junit.Assert;

import java.util.Map;

public class PsiTest extends AndroidTestCase {

    public void test_useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.spgrouptest.psidisplay", appContext.getPackageName());
    }

    public void test_processPsi() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final PsiParser psiParser = new PsiParser(appContext);
        psiParser.processPsi(new PsiListener() {
            @Override
            public void onSuccess() {
                Map<String, String> east = psiParser.getEast();
                Map<String, String> west = psiParser.getWest();
                Map<String, String> north = psiParser.getNorth();
                Map<String, String> south = psiParser.getSouth();
                Map<String, String> central = psiParser.getCentral();

                assertTrue(east.keySet().size() > 0);
                assertTrue(west.keySet().size() > 0);
                assertTrue(north.keySet().size() > 0);
                assertTrue(south.keySet().size() > 0);
                assertTrue(central.keySet().size() > 0);
            }

            @Override
            public void onError(String errorMessage) {
                Assert.assertTrue(errorMessage, false);
            }
        });
    }
}
