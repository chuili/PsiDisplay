package com.spgrouptest.psidisplay.internal;

public class Result<T> {

    private final static int ERROR_CODE_EXCEPTION = -1;
    private final T result;
    private final boolean isSuccessful;
    private final int errorCode;
    private final String errorMessage;

    public Result(final T result, final boolean isSuccessful, final int errorCode, final String errorMessage) {
        this.result = result;
        this.isSuccessful = isSuccessful;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Result(final int errorCode) {
        this(null, false, errorCode, null);
    }

    public Result(final String errorMessage) {
        this(null, false, ERROR_CODE_EXCEPTION, errorMessage);
    }

    public Result(int errorCode, String errorMessage) {
        this(null, false, errorCode, errorMessage);
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public T getResult() {
        return this.result;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
