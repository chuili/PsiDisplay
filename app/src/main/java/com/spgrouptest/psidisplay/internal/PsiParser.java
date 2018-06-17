package com.spgrouptest.psidisplay.internal;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PsiParser {

    private static final String TAG = PsiParser.class.getName();

    private Handler mainThreadHandler;
    private Map<String, String> east;
    private Map<String, String> west;
    private Map<String, String> north;
    private Map<String, String> south;
    private Map<String, String> central;
    private String status;

//    public PsiInfo getPsiInfoEast() {
//        return psiInfoEast;
//    }
//
//    public PsiInfo getPsiInfoWest() {
//        return psiInfoWest;
//    }
//
//    public PsiInfo getPsiInfoNorth() {
//        return psiInfoNorth;
//    }
//
//    public PsiInfo getPsiInfoSouth() {
//        return psiInfoSouth;
//    }
//
//    public PsiInfo getPsiInfoCentral() {
//        return psiInfoCentral;
//    }

    public PsiParser(Context context) {
        east = new HashMap<String, String>();
        west = new HashMap<String, String>();
        north = new HashMap<String, String>();
        south = new HashMap<String, String>();
        central = new HashMap<String, String>();
        mainThreadHandler = new Handler(context.getMainLooper());
    }

    public Map<String, String> getEast() {
        return east;
    }

    public Map<String, String> getWest() {
        return west;
    }

    public Map<String, String> getNorth() {
        return north;
    }

    public Map<String, String> getSouth() {
        return south;
    }

    public Map<String, String> getCentral() {
        return central;
    }

    public String getStatus() {
        return status;
    }

    public void processPsi(final PsiListener listener) {
        Log.d(TAG, "getPsi()");
        final GetPsiCommand getPsiCommand = new GetPsiCommand();

        final Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Result<CommandResponse> result;

                    result = getPsiCommand.execute();
                    if (result != null) {
                        if (result.isSuccessful()) {
                            final CommandResponse response = result.getResult();
                            if (response instanceof GetPsiResponse) {
                                final String regionMetadata = ((GetPsiResponse) response).getRegionMetadata();
                                parseRegionMetadata(regionMetadata);

                                final String items = ((GetPsiResponse) response).getItems();
                                parseItems(items);

                                final String apiInfo = ((GetPsiResponse) response).getApiInfo();
                                parseApiInfo(apiInfo);

                                mainThreadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onSuccess();
                                        }
                                    }
                                });
                            } else {
                                mainThreadHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onError("Internal Error");
                                        }
                                    }
                                });
                            }
                        } else {
                            mainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null) {
                                        listener.onError(result.getErrorMessage());
                                    }
                                }
                            });
                        }
                    } else {
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onError("Internal Error");
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        workerThread.start();
    }

    private void parseRegionMetadata(String regionMetadata) throws JSONException {
        Log.d(TAG, "parseRegionMetadata()");
        JSONArray regionDataJsonArray = new JSONArray(regionMetadata);

        if (regionDataJsonArray == null || regionDataJsonArray.length() == 0) {
            throw new JSONException("regionDataJsonArray is null.");
        }

        for (int i=0; i<regionDataJsonArray.length(); i++) {
            if (regionDataJsonArray.isNull(i)) {
                throw new JSONException("regionData is null.");
            }

            final JSONObject regionDataJson = regionDataJsonArray.getJSONObject(i);
//            PsiInfo currentPsiInfo;
            Map<String, String> currHashMap;
            switch (regionDataJson.getString(GetPsiCommand.RESP_PARAM_NAME)) {
                case "east":
//                    currentPsiInfo = psiInfoEast;
                    currHashMap = east;
                    break;
                case "west":
//                    currentPsiInfo = psiInfoWest;
                    currHashMap = west;
                    break;
                case "north":
//                    currentPsiInfo = psiInfoNorth;
                    currHashMap = north;
                    break;
                case "south":
//                    currentPsiInfo = psiInfoSouth;
                    currHashMap = south;
                    break;
                case "central":
//                    currentPsiInfo = psiInfoCentral;
                    currHashMap = central;
                    break;
                default:
//                    currentPsiInfo = new PsiInfo();
                    currHashMap = new HashMap<String, String>();
                    Log.d(TAG, "Undefined region: " + regionDataJson.get(GetPsiCommand.RESP_PARAM_NAME).toString());
            }

            final JSONObject labelLocation = regionDataJson.getJSONObject(GetPsiCommand.RESP_PARAM_LABEL_LOCATION);
            final String latitude = labelLocation.getString(GetPsiCommand.RESP_PARAM_LATITUDE);
            final String longitude = labelLocation.getString(GetPsiCommand.RESP_PARAM_LONGITUDE);

//            currentPsiInfo.setLatitude(latitude);
//            currentPsiInfo.setLongitude(longitude);

            currHashMap.put(GetPsiCommand.RESP_PARAM_LATITUDE, latitude);
            currHashMap.put(GetPsiCommand.RESP_PARAM_LONGITUDE, longitude);
        }
    }

    private void parseItems(String items) throws JSONException {
        Log.d(TAG, "parseItems()");
        JSONArray itemsJsonArray = new JSONArray(items);

        if (itemsJsonArray == null || itemsJsonArray.length() == 0) {
            throw new JSONException("itemsJsonArray is null.");
        }

        Log.d(TAG, "items length: " + itemsJsonArray.length());
        for (int i=0; i<itemsJsonArray.length(); i++) {
            if (itemsJsonArray.isNull(i)) {
                throw new JSONException("readings is null.");
            }

            JSONObject itemsJson = itemsJsonArray.getJSONObject(i);
            Log.d(TAG, "itemsJson: " + itemsJson.toString());
            JSONObject readingsJson = itemsJson.getJSONObject(GetPsiCommand.RESP_PARAM_READINGS);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_O3_SUB_INDEX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_PM10_TWENTY_FOUR_HOURLY);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_PM10_SUB_INDEX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_CO_SUB_INDEX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_PM25_TWENTY_FOUR_HOURLY);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_SO2_SUB_INDEX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_CO_EIGHT_HOUR_MAX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_NO2_ONE_HOUR_MAX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_SO2_TWENTY_FOUR_HOURLY);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_PM25_SUB_INDEX);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_PSI_TWENTY_FOUR_HOURLY);
            parseReadings(readingsJson, GetPsiCommand.RESP_PARAM_O3_EIGHT_HOUR_MAX);
        }
    }

    private void parseReadings(JSONObject readingsJson, String key) throws JSONException {
        Log.d(TAG, "parseReadings(): " + key);
        if (readingsJson.has(key)) {
            JSONObject reading = readingsJson.getJSONObject(key);
            Log.d(TAG, "reading: " + reading.toString());

            if (reading.has(GetPsiCommand.RESP_PARAM_WEST)) {
//                psiInfoWest.setO3_sub_index(reading.getInt(GetPsiCommand.RESP_PARAM_WEST));
                west.put(key, reading.getString(GetPsiCommand.RESP_PARAM_WEST));
            }

            if (reading.has(GetPsiCommand.RESP_PARAM_EAST)) {
//                psiInfoEast.setO3_sub_index(reading.getInt(GetPsiCommand.RESP_PARAM_EAST));
                east.put(key, reading.getString(GetPsiCommand.RESP_PARAM_EAST));
            }

            if (reading.has(GetPsiCommand.RESP_PARAM_CENTRAL)) {
//                psiInfoCentral.setO3_sub_index(reading.getInt(GetPsiCommand.RESP_PARAM_CENTRAL));
                central.put(key, reading.getString(GetPsiCommand.RESP_PARAM_CENTRAL));
            }

            if (reading.has(GetPsiCommand.RESP_PARAM_SOUTH)) {
//                psiInfoSouth.setO3_sub_index(reading.getInt(GetPsiCommand.RESP_PARAM_SOUTH));
                south.put(key, reading.getString(GetPsiCommand.RESP_PARAM_SOUTH));
            }

            if (reading.has(GetPsiCommand.RESP_PARAM_NORTH)) {
//                psiInfoNorth.setO3_sub_index(reading.getInt(GetPsiCommand.RESP_PARAM_NORTH));
                north.put(key, reading.getString(GetPsiCommand.RESP_PARAM_NORTH));
            }
        }
    }

    private void parseApiInfo(String apiInfo) throws JSONException {
        Log.d(TAG, "parseApiInfo()");
        JSONObject apiInfoJson = new JSONObject(apiInfo);
        final String status = apiInfoJson.getString(GetPsiCommand.RESP_PARAM_STATUS);
        this.status = status;
    }
}
