package com.spgrouptest.psidisplay.internal;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class GetPsiCommand extends AbstractServerCommand {

    private static final String TAG = GetPsiCommand.class.getName();

    // Class variables - URL
    private static final String URL_GET_PSI_COMMAND = "https://api.data.gov.sg/v1/environment/psi";

    // Class variables - Request Params
    private static final String REQ_PARAM_DATE_TIME = "date_time";
    private static final String REQ_PARAM_DATE = "date";

    // Class variables - Response Params
    private static final String RESP_PARAM_REGION_METADATA = "region_metadata";
    public static final String RESP_PARAM_NAME = "name";
    public static final String RESP_PARAM_LABEL_LOCATION = "label_location";
    public static final String RESP_PARAM_LATITUDE = "latitude";
    public static final String RESP_PARAM_LONGITUDE = "longitude";

    private static final String RESP_PARAM_ITEMS = "items";
    public static final String RESP_PARAM_READINGS = "readings";
    public static final String RESP_PARAM_O3_SUB_INDEX = "o3_sub_index";
    public static final String RESP_PARAM_PM10_TWENTY_FOUR_HOURLY = "pm10_twenty_four_hourly";
    public static final String RESP_PARAM_PM10_SUB_INDEX = "pm10_sub_index";
    public static final String RESP_PARAM_CO_SUB_INDEX = "co_sub_index";
    public static final String RESP_PARAM_PM25_TWENTY_FOUR_HOURLY = "pm25_twenty_four_hourly";
    public static final String RESP_PARAM_SO2_SUB_INDEX = "so2_sub_index";
    public static final String RESP_PARAM_CO_EIGHT_HOUR_MAX = "co_eight_hour_max";
    public static final String RESP_PARAM_NO2_ONE_HOUR_MAX = "no2_one_hour_max";
    public static final String RESP_PARAM_SO2_TWENTY_FOUR_HOURLY = "so2_twenty_four_hourly";
    public static final String RESP_PARAM_PM25_SUB_INDEX = "pm25_sub_index";
    public static final String RESP_PARAM_PSI_TWENTY_FOUR_HOURLY = "psi_twenty_four_hourly";
    public static final String RESP_PARAM_O3_EIGHT_HOUR_MAX = "o3_eight_hour_max";
    public static final String RESP_PARAM_WEST = "west";
    public static final String RESP_PARAM_EAST = "east";
    public static final String RESP_PARAM_CENTRAL = "central";
    public static final String RESP_PARAM_SOUTH = "south";
    public static final String RESP_PARAM_NORTH = "north";

    private static final String RESP_PARAM_API_INFO = "api_info";
    public static final String RESP_PARAM_STATUS = "status";

    private static final String RESP_PARAM_MESSAGE = "message";

    public GetPsiCommand() {
        super(URL_GET_PSI_COMMAND, HttpRequestMethods.GET.toString());
    }

    @Override
    protected Map<String, String> prepareRequest() {
        final Map<String, String> requestEntity = new HashMap<>();
        return requestEntity;
    }

    @Override
    protected Result<CommandResponse> processResponse (CommandResponse response) {
        final GetPsiResponse getPsiResponse = new GetPsiResponse(response.getHttpResponseCode());
        final Map<String, String> dict = response.getDataDictionary();
        Log.d(TAG, "GetPsiCommand - processResponse");
        Log.d(TAG, "getHttpResponseCode: " + response.getHttpResponseCode());

        if (dict.containsKey(RESP_PARAM_MESSAGE)) {
            return new Result<CommandResponse>(null, false,
                    response.getHttpResponseCode(), dict.get(RESP_PARAM_MESSAGE));
        }

        getPsiResponse.setRegionMetadata(dict.get(RESP_PARAM_REGION_METADATA));
        getPsiResponse.setItems(dict.get(RESP_PARAM_ITEMS));
        getPsiResponse.setApiInfo(dict.get(RESP_PARAM_API_INFO));

        return new Result<CommandResponse>(getPsiResponse, true, 0, null);
    }
}
