package com.springapp.demo.model;

/**
 * Created by tmeehan on 2/18/15.
 */
public enum Price {
    $(1),
    $$(2),
    $$$(3),
    $$$$(4);

    private final String queryString;
    Price(Integer price) {
        this.queryString = price.toString();
    }

    public String getQueryString() {
        return queryString;
    }
}
