package com.eagle.fragmentsample.model;

import com.eagle.fragmentsample.R;

import java.util.ArrayList;
import java.util.List;

public class Countur {

    private int imageId;
    private String title;
    private String description;

    private List<Lamp> lamps;

    public Countur(String title, String description, int imageId) {
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public Countur() {
    }

    public static final Countur[] countur = {
            new Countur("Освещение Кухня - 94663", "Светодиодная лента SMD 5050 RGB на базе светодиодов Epistar ", R.drawable.lamp_up),
            new Countur("Освещение Спальня - 94664", "Подвесной потолочный светильник, предназначенный для создания бестеневого освещения", R.drawable.lamp_up),
            new Countur("Освещение Санузел №1 - 94665", "Искусственный источник света, прибор, перераспределяющий свет лампы",R.drawable.lamp_up),
            new Countur("Освещение Гостиная - 94737", "",R.drawable.lamp_up)
    };

    public List<Lamp> getLamps() {
        return lamps;
    }

    public void setLamps(List<Lamp> lamps) {
        this.lamps = lamps;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public static ArrayList<String> getTitles(){

        ArrayList<String> dataList = new ArrayList<>();

        for (Countur countur : Countur.countur) {
            String strTitle = countur.getTitle();
            dataList.add(strTitle);
        }
        return dataList;
    }

//    public ArrayList<String> getCountur(){
//        ArrayList<String> dataCountur = new ArrayList<>();
//        for (Countur countur: Countur.this){
//
//        }
//        return dataCountur;
//    }
}
