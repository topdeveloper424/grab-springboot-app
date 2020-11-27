package com.graberWebApp.graber.model;

public class GrabData {
    private final int id;
    private final int serp_id;
    private final String url;
    private final String jsonArrData;
    private final String serp_title;
    private final String serp_description;
    private final short organic_pos;
    private final short block_pos;
    private final String date_added;
    private final String date_updated;

    public GrabData(int id, int serp_id, String url, String jsonArrData, String serp_title, String serp_description, short organic_pos, short block_pos, String date_added, String date_updated){
        this.id = id;
        this.serp_id = serp_id;
        this.url = url;
        this.jsonArrData = jsonArrData;
        this.serp_title = serp_title;
        this.serp_description = serp_description;
        this.organic_pos = organic_pos;
        this.block_pos = block_pos;
        this.date_added = date_added;
        this.date_updated = date_updated;
    }

    public int getId(){
        return this.id;
    }

    public String getUrl(){
        return this.url;
    }

    public String getJsonArrData(){
        return this.jsonArrData;
    }

    public String getDate_added(){
        return this.date_added;
    }

    public String getDate_updated(){
        return this.date_updated;
    }

    public int getSerp_id() {
        return serp_id;
    }

    public String getSerp_title() {
        return serp_title;
    }

    public String getSerp_description() {
        return serp_description;
    }

    public short getOrganic_pos() {
        return organic_pos;
    }

    public short getBlock_pos() {
        return block_pos;
    }
}
