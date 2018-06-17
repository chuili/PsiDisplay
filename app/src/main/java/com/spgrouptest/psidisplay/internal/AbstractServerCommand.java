package com.spgrouptest.psidisplay.internal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AbstractServerCommand {

    private static final String TAG = AbstractServerCommand.class.getName();
    private URL url;
    private String requestMethod;

    public AbstractServerCommand() {
        // Nothing to initialize
    }

    public AbstractServerCommand(String url, String method) {
        try {
            this.url = new URL(url);
            this.requestMethod = method;
        } catch (MalformedURLException e) {
            Log.d(TAG, "Malformed URL!");
            e.printStackTrace();
        }

    }

    private Result<CommandResponse> doHttpRequest() {
        Result<CommandResponse> result = null;

        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod(requestMethod);

            result = handleServerResponse(connection);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }
        return result;
    }

    private Result<CommandResponse> handleServerResponse(HttpURLConnection connection) throws IOException {
        Result<CommandResponse> result = null;
        InputStream inputStream = null;
        BufferedReader inputStreamReader = null;
        try {
            final int responseCode = connection.getResponseCode();
            Log.d(TAG, "Server response code: " + responseCode);

            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_BAD_REQUEST: {
                    inputStream = connection.getInputStream();
                    inputStreamReader = new BufferedReader(new InputStreamReader(inputStream));
                    final StringBuffer responseBuffer = new StringBuffer();
                    String inputStr = "";

                    while (inputStr != null) {
                        responseBuffer.append(inputStr);
                        inputStr = inputStreamReader.readLine();
                    }

                    final CommandResponse cmdResponse = new CommandResponse(responseCode);
                    processCmdResponse(cmdResponse, responseBuffer.toString());
                    result = new Result<CommandResponse>(cmdResponse, true, 0, null);
                }
                break;
                default: {
                    result = new Result<CommandResponse>(responseCode);
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "IOException " + e.getMessage());
            result = new Result<>(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to close the inputStream stream in the finally block. Simply skip it...");
            }
        }
        return result;
    }

    private byte[] createJSONRequestData() {
        final Map<String, String> jsonRequestEntities = prepareRequest();

        byte[] data = null;
        if (jsonRequestEntities != null) {
            try {
                final JSONObject jsonObject = new JSONObject();
                for (final String key : jsonRequestEntities.keySet()) {
                    final String value = jsonRequestEntities.get(key);
                    if (value != null && !value.isEmpty() && value.charAt(0) == '{'
                            && value.charAt(value.length()-1) == '}') {
                        final JSONObject valueJson = new JSONObject(value);
                        jsonObject.put(key, valueJson);
                    } else {
                        jsonObject.put(key, jsonRequestEntities.get(key));
                    }
                }

                final String dataStr = jsonObject.toString();
                Log.i(TAG, "executeRequest data " + dataStr);
                data = dataStr.getBytes();
                jsonRequestEntities.clear();
            } catch (JSONException e) {
                Log.d(TAG, "JSONException: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return data;
    }

    private void processCmdResponse(final CommandResponse cmdResponse,
                                    final String responseStr) {
        try {
            Log.i(TAG, "processResponse : " + responseStr);

            if (responseStr == null || responseStr.length() == 0) {
                Log.d(TAG, "Null response string!");
                return;
            }
            final JSONObject respObj = new JSONObject(responseStr);
            final Iterator<?> iter = respObj.keys();
            while (iter.hasNext()) {
                final String key = (String) iter.next();
                final String value = respObj.getString(key);

                Log.d(TAG, "processCmdResponse KEY: " + key + " VALUE: " + value);
                cmdResponse.getDataDictionary().put(key, value);
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected Map<String, String> prepareRequest() {
        final Map<String, String> jsonRequestEntities = new HashMap<String, String>();
        return jsonRequestEntities;
    }

    public final Result<CommandResponse> execute() {
        Log.d(TAG, ".execute() is called");

        Result<CommandResponse> result = doHttpRequest();
        Log.d(TAG, ".doHttpRequest() is called");
        Log.d(TAG, ".getResult() is called before3");
        final CommandResponse cmdResp = result.getResult();
        Log.d(TAG, ".getResult() is called after3");

        if (cmdResp == null) {
            Log.e(TAG, "CommandResponse is null");
        }

        if (!result.isSuccessful()) {
            return result;
        }
        return processResponse(cmdResp);
    }

    protected Result<CommandResponse> processResponse(final CommandResponse commandResp) {
        return new Result<CommandResponse>(commandResp, true, 0, null);
    }
}
