package com.spgrouptest.psidisplay.internal;

public class GetPsiResponse extends CommandResponse {

    private static final String TAG = GetPsiResponse.class.getName();

    // Class Variables
    private String regionMetadata;
    private String items;
    private String apiInfo;
//    private PsiInfo psiInfoNorth;
//    private PsiInfo psiInfoSouth;
//    private PsiInfo psiInfoEast;
//    private PsiInfo psiInfoWest;
//    private PsiInfo psiInfoCentral;


    public GetPsiResponse(final int httpResponseCode) {
        super(httpResponseCode);
    }

    public String getRegionMetadata() {
        return regionMetadata;
    }

    public void setRegionMetadata(final String regionMetadata) {
        this.regionMetadata = regionMetadata;
    }

    public String getItems() {
        return items;
    }

    public void setItems(final String items) {
        this.items = items;
    }

    public String getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(final String apiInfo) {
        this.apiInfo = apiInfo;
    }
}
