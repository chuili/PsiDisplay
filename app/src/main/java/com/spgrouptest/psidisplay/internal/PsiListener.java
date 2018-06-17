package com.spgrouptest.psidisplay.internal;

public interface PsiListener {

    /**
     * Callback method invoked when get PSI command is completed
     */
    void onSuccess();

    /**
     * Callback method invoked when error is encountered in get PSI command
     */
    void onError(String errorMessage);
}
