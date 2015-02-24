package com.springapp.demo.model;

/**
 * Created by tmeehan on 2/18/15.
 */
public enum Section {
    FOOD("food"),
    DRINKS("drinks"),
    COFFEE("coffee"),
    SHOPS("shops"),
    ARTS("arts"),
    OUTDOORS("outdoors"),
    SIGHTS("sights"),
    TRENDING("trending"),
    SPECIALS("specials"),
    NEXT_VENUES("nextVenues"),
    TOP_PICKS("topPicks");

    private final String queryName;
    Section(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryName() {
        return queryName;
    }
}
