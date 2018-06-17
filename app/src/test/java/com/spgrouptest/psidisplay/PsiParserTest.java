package com.spgrouptest.psidisplay;

import android.content.Context;
import android.util.Log;

import com.spgrouptest.psidisplay.internal.PsiParser;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class PsiParserTest {

    @Mock
    Context mMockContext;

    @Test
    public void test_parseRegionMetadata_Nominal() {
        try {
            PowerMockito.mockStatic(Log.class);
            PsiParser psiParser = new PsiParser(mMockContext);
            String regionData = "[{\"name\":\"west\",\"label_location\":{\"latitude\":1.35735,\"longitude\":103.7}},{\"name\":\"national\",\"label_location\":{\"latitude\":0,\"longitude\":0}},{\"name\":\"east\",\"label_location\":{\"latitude\":1.35735,\"longitude\":103.94}},{\"name\":\"central\",\"label_location\":{\"latitude\":1.35735,\"longitude\":103.82}},{\"name\":\"south\",\"label_location\":{\"latitude\":1.29587,\"longitude\":103.82}},{\"name\":\"north\",\"label_location\":{\"latitude\":1.41803,\"longitude\":103.82}}]";
            PowerMockito.verifyPrivate(psiParser).invoke("parseRegionMetadata", regionData);
            Assert.assertNotNull(psiParser.getEast());
            Assert.assertNotNull(psiParser.getWest());
            Assert.assertNotNull(psiParser.getNorth());
            Assert.assertNotNull(psiParser.getSouth());
            Assert.assertNotNull(psiParser.getCentral());
        } catch (Exception e) {
            System.out.println("Unexpected: Exception");
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void test_parseRegionMetadata_NullString() {
        try {
            PowerMockito.mockStatic(Log.class);
            PsiParser psiParser = new PsiParser(mMockContext);
            PowerMockito.verifyPrivate(psiParser).invoke("parseRegionMetadata", null);
        } catch (NullPointerException e) {
            System.out.println("Expected: NullPointerException");
            Assert.assertTrue("regionMetadata is null.".equalsIgnoreCase(e.getMessage()));
        } catch (Exception e) {
            System.out.println("Unexpected: Exception");
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void test_parseRegionMetadata_NullJsonArray() {
        try {
            PowerMockito.mockStatic(Log.class);
            PsiParser psiParser = new PsiParser(mMockContext);
            String regionData = "[]";
            PowerMockito.verifyPrivate(psiParser).invoke("parseRegionMetadata", regionData);
        } catch (JSONException e) {
            System.out.println("Expected: JSONException");
            e.printStackTrace();
            Assert.assertTrue("regionDataJsonArray is null.".equalsIgnoreCase(e.getMessage()));
        } catch (Exception e) {
            System.out.println("Unexpected: Exception");
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void test_parseItems_Nominal() {
        try {
            PowerMockito.mockStatic(Log.class);
            PsiParser psiParser = new PsiParser(mMockContext);
            String items = "[{\"timestamp\":\"2018-06-17T20:00:00+08:00\",\"update_timestamp\":\"2018-06-17T20:03:52+08:00\",\"readings\":{\"o3_sub_index\":{\"west\":18,\"national\":21,\"east\":16,\"central\":16,\"south\":18,\"north\":21},\"pm10_twenty_four_hourly\":{\"west\":21,\"national\":32,\"east\":23,\"central\":32,\"south\":30,\"north\":20},\"pm10_sub_index\":{\"west\":21,\"national\":32,\"east\":23,\"central\":32,\"south\":30,\"north\":20},\"co_sub_index\":{\"west\":4,\"national\":6,\"east\":4,\"central\":5,\"south\":6,\"north\":3},\"pm25_twenty_four_hourly\":{\"west\":11,\"national\":16,\"east\":10,\"central\":15,\"south\":16,\"north\":12},\"so2_sub_index\":{\"west\":55,\"national\":55,\"east\":3,\"central\":3,\"south\":9,\"north\":3},\"co_eight_hour_max\":{\"west\":0.44,\"national\":0.56,\"east\":0.39,\"central\":0.46,\"south\":0.56,\"north\":0.3},\"no2_one_hour_max\":{\"west\":33,\"national\":33,\"east\":22,\"central\":25,\"south\":13,\"north\":26},\"so2_twenty_four_hourly\":{\"west\":105,\"national\":105,\"east\":5,\"central\":5,\"south\":15,\"north\":4},\"pm25_sub_index\":{\"west\":46,\"national\":56,\"east\":43,\"central\":54,\"south\":56,\"north\":49},\"psi_twenty_four_hourly\":{\"west\":55,\"national\":56,\"east\":43,\"central\":54,\"south\":56,\"north\":49},\"o3_eight_hour_max\":{\"west\":42,\"national\":49,\"east\":38,\"central\":38,\"south\":43,\"north\":49}}}]";
            PowerMockito.verifyPrivate(psiParser).invoke("parseItems", items);
            Assert.assertNotNull(psiParser.getEast());
            Assert.assertNotNull(psiParser.getWest());
            Assert.assertNotNull(psiParser.getNorth());
            Assert.assertNotNull(psiParser.getSouth());
            Assert.assertNotNull(psiParser.getCentral());
        } catch (Exception e) {
            System.out.println("Unexpected: Exception");
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void test_parseApiInfo_Nominal() {
        try {
            PowerMockito.mockStatic(Log.class);
            PsiParser psiParser = new PsiParser(mMockContext);
            String apiInfo = "{\"status\":\"healthy\"}";
            PowerMockito.verifyPrivate(psiParser).invoke("parseApiInfo", apiInfo);
            Assert.assertTrue("healthy".equalsIgnoreCase(psiParser.getStatus()));
        } catch (Exception e) {
            System.out.println("Unexpected: Exception");
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}