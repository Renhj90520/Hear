package com.jamendo.renhaojie.hear.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ren Haojie on 2017/1/6.
 */

public class Response<T> {
    private Headers headers;
    private List<T> results = new ArrayList<>();

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
