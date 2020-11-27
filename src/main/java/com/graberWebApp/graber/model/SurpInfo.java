package com.graberWebApp.graber.model;

public class SurpInfo {
    private final int id;
    private final String keyword;
    private final String location;
    private final String search_engine;
    private final String search_lang;
    private final String device;
    private final String serpArrData;
    private final String organic;
    private final String people_also_ask;
    private final String related_searches;
    private final String featured_snippet;
    private final String fetch_date;

    public SurpInfo(int id, String keyword, String location, String search_engine, String search_lang, String device, String serpArrData, String organic, String people_also_ask, String related_searches, String featured_snippet, String fetch_date) {
        this.id = id;
        this.keyword = keyword;
        this.location = location;
        this.search_engine = search_engine;
        this.search_lang = search_lang;
        this.device = device;
        this.serpArrData = serpArrData;
        this.organic = organic;
        this.people_also_ask = people_also_ask;
        this.related_searches = related_searches;
        this.featured_snippet = featured_snippet;
        this.fetch_date = fetch_date;
    }

    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLocation() {
        return location;
    }

    public String getSearch_engine() {
        return search_engine;
    }

    public String getSearch_lang() {
        return search_lang;
    }

    public String getDevice() {
        return device;
    }

    public String getSerpArrData() {
        return serpArrData;
    }

    public String getOrganic() {
        return organic;
    }

    public String getPeople_also_ask() {
        return people_also_ask;
    }

    public String getRelated_searches() {
        return related_searches;
    }

    public String getFeatured_snippet() {
        return featured_snippet;
    }

    public String getFetch_date() {
        return fetch_date;
    }
}
