package com.jamendo.renhaojie.hear.models;

public class Headers {
    private String error_message;
    private int code;
    private int results_count;
    private String warnings;
    private String status;

    public String getError_message() {
        return this.error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResults_count() {
        return this.results_count;
    }

    public void setResults_count(int results_count) {
        this.results_count = results_count;
    }

    public String getWarnings() {
        return this.warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
