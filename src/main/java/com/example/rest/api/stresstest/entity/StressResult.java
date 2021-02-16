package com.example.rest.api.stresstest.entity;

import org.springframework.http.HttpStatus;

public class StressResult {
    
    String url;
    String elapsedTime;
    HttpStatus httpStatus;
    boolean result;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.result = true;
        if(httpStatus != HttpStatus.OK){
            this.result = false;
        }
    }

    public boolean getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "StressResult [ result=" + result 
                                + ", elapsedTime=" + elapsedTime 
                                + ", httpStatus=" + httpStatus 
                                + ", url=" + url + "]";
    }
    
}

