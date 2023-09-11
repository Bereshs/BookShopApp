package com.example.MyBookShopApp.data.dto;

public class ApiSimpleResponse {
    private boolean result;

    public ApiSimpleResponse(boolean result) {
        this.result =  result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
