package com.eagle.fragmentsample.model;


import java.util.ArrayList;

public class Lamp {

    private int type;
    private boolean sw;
    private String id;

    public Lamp() {
    }

    public Lamp(String id, int type, boolean sw) {
        this.id = id;
        this.type = type;
        this.sw = sw;
    }

    public static final Lamp[] lamp = {
            new Lamp("94738",1,false),
            new Lamp("94739",1,false),
            new Lamp("94740",0,false)
    };



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSw() {
        return sw;
    }

    public void setSw(boolean sw) {
        this.sw = sw;
    }

    public static ArrayList<String> getTitles(){

        ArrayList<String> dataList = new ArrayList<>();

        for (Lamp lamp : Lamp.lamp) {
            String strTitle = lamp.getId();
            dataList.add(strTitle);
        }
        return dataList;
    }




}
