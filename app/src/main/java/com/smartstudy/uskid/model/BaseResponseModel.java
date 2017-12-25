package com.smartstudy.uskid.model;


/**
 * 返回值的基类
 *
 * @param <T>
 */
public class BaseResponseModel<T> {
    private String message;
    private Integer error_code;
    private boolean success;


    private T items;

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
