package com.smartstudy.uskid.library.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author 王宏杰
 * @date 2018/3/23
 */

public class ApiError extends RuntimeException {

    public static final int NO_DATA = 200001;
    @SerializedName("error_code")
    private Integer code;
    @SerializedName("message")
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiError(String resultMessage) {
        super(resultMessage);
    }

    public boolean isError() {
        return code != null && code > 1;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
