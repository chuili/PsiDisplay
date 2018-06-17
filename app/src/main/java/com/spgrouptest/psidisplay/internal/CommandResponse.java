package com.spgrouptest.psidisplay.internal;

import java.util.HashMap;
import java.util.Map;

public class CommandResponse {
    private final int httpResponseCode;
    private final Map<String, String> dataDict;

    public CommandResponse(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
        dataDict = new HashMap<String, String>();
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public Map<String, String> getDataDictionary() {
        return dataDict;
    }
}
